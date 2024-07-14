package org.fate.faterpc.loadbalancer;

import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器
 * @Author: Fate
 * @Date: 2024/7/13 15:12
 **/
public interface LoadBalancer
{
    /**
     * 选择服务
     * @param requestParams 请求参数,用于计算hash值  根据hash值选择服务提供者
     * @param serviceMetaInfoList 服务元信息列表
     * @return 选择的服务提供者
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
