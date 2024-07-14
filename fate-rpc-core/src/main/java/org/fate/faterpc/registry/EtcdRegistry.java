package org.fate.faterpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.vertx.core.impl.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: etcd注册中心
 * @Author: Fate
 * @Date: 2024/7/11 20:10
 **/
@Slf4j
public class EtcdRegistry implements Registry {

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 监听的key集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * etcd 客户端
     */
    private Client client;

    /**
     * etcd KV 客户端
     */
    private KV kvClient;

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        HeartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建租约 30分钟后过期
        long leaseId = leaseClient.grant(30 * 60).get(5, TimeUnit.SECONDS).getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约绑定 并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 添加节点信息到本地缓存
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 删除节点
        String registryName = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryName, StandardCharsets.UTF_8)).get();
        log.info("删除etcd节点成功:{}",registryName);

        // 从本地缓存中删除节点信息
        localRegisterNodeKeySet.remove(registryName);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        // 优先从本地缓存中获取服务信息
        List<ServiceMetaInfo> CachedServiceMetaInfoList = registryServiceCache.readCache();
        if(CachedServiceMetaInfoList != null && !CachedServiceMetaInfoList.isEmpty()){
            log.info("从本地缓存中获取服务信息:{}",CachedServiceMetaInfoList);
            return CachedServiceMetaInfoList;
        }

        // 缓存中没有，从etcd中获取服务信息
        // 前缀搜索
        String servicePrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence
                            .from(servicePrefix,StandardCharsets.UTF_8),getOption)
                            .get()
                            .getKvs();
            // 解析结果
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(kv -> {
                        String key = kv.getKey().toString(StandardCharsets.UTF_8);
                        // 监听该节点
                        watch(key);
                        String value = kv.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());

            // 缓存服务信息到本地
            registryServiceCache.writeCache(serviceMetaInfoList);

            log.info("从Etcd中获取服务信息:{}",serviceMetaInfoList);
            return serviceMetaInfoList;
        }catch (Exception e){
            throw new RuntimeException("获取服务信息失败",e);
        }
    }

    @Override
    public void destroy() {
        log.info("当前服务停止,释放Etcd资源");

        // 节点停止
        // 遍历本节点所有的 key
        for (String key : localRegisterNodeKeySet) {
            try {
                // 删除节点
                DeleteResponse deleteResponse = kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
                if (deleteResponse.getDeleted() > 0) {
                    log.info("删除Etcd节点成功:{},异步删除结果状态码:{}", key, deleteResponse.getDeleted());
                }else {
                    log.info("删除Etcd节点失败,{}",deleteResponse);
                }
            } catch (Exception e) {
                log.error("删除Etcd节点失败:{},异常信息:{}", key, e.getMessage());
            }
        }

        // 释放etcd资源
        if (kvClient != null){
            kvClient.close();
        }

        if (client != null) {
            client.close();
        }
    }

    @Override
    public void HeartBeat() {
        // 定时任务，每隔25分钟续签一次
        CronUtil.schedule("* */25 * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听服务变化
     * @param serviceNodeKey 服务节点key
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();

        // 监听未被监听的key
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8),watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()){
                    switch (event.getEventType()){
                        // 当删除节点时触发
                        case DELETE:
                            // 清除本地缓存
                            log.info("监听到etcd节点删除3:{}",serviceNodeKey);
                            registryServiceCache.clearCache(serviceNodeKey);
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }
}
