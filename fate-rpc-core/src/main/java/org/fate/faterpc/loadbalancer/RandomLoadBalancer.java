package org.fate.faterpc.loadbalancer;

import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * 随机负载均衡策略
 * @Author: Fate
 * @Date: 2024/7/13 15:20
 **/
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();

        if(size == 0){
            return null;
        }

        if(size == 1){
            return serviceMetaInfoList.get(0);
        }

        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
