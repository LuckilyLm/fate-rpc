package org.fate.faterpc.loadbalancer;

import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 轮询负载均衡
 * @Author: Fate
 * @Date: 2024/7/13 15:17
 **/
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 轮询下标
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {

        if(serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()){
            return null;
        }

        int size = serviceMetaInfoList.size();
        // 如果只有一个服务，则直接返回
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }

        // 计算下标
        int index = currentIndex.getAndIncrement() % size;

        return serviceMetaInfoList.get(index);
    }
}
