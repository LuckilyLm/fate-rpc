package org.fate.faterpc.registry;

import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @Description: 注册中心接口
 * @Author: Fate
 * @Date: 2024/7/11 18:24
 **/

public interface Registry
{
    /**
     * 初始化
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo 服务元信息
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo 服务元信息
     */
    void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现
     * @param serviceKey 服务key
     * @return 服务元信息列表
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务注销
     */
    void destroy();

    /**
     * 心跳检测
     */
    void HeartBeat();

    /**
     * 监听服务变化
     */
    void watch(String serviceNodeKey);

}
