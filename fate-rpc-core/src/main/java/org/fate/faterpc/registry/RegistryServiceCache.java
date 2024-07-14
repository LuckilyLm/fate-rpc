package org.fate.faterpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务缓存
 * @Author: Fate
 * @Date: 2024/7/12 16:26
 **/
@Slf4j
public class RegistryServiceCache
{
    /**
     * 服务信息缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 存缓存
     * @param serviceCache 缓存
     */
    void writeCache(List<ServiceMetaInfo> serviceCache){
        this.serviceCache = serviceCache;
    }

    /**
     * 读取缓存
     * @return 缓存
     */
    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    /**
     * 清除缓存
     */
    void clearCache(String serviceNodeKey){
        // TODO 由于使用的是List,可能会存在并发问题,需要加锁. 或者使用Vector或CopyOnWriteArrayLis来替代List
        // 这里暂时先不加锁，等后续有需要再优化
        serviceCache.removeIf(serviceMetaInfo -> serviceMetaInfo.getServiceKey().equals(serviceNodeKey));
        log.info("本地缓存清除成功:{}", serviceNodeKey);
        log.info("当前本地缓存信息:{}", serviceCache);
    }

}
