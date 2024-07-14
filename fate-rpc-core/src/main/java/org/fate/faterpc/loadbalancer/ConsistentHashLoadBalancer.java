package org.fate.faterpc.loadbalancer;

import org.fate.faterpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 一致性Hash负载均衡算法
 * @Author: Fate
 * @Date: 2024/7/13 15:23
 **/
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性Hash环 存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {

        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 构建一致性Hash环，添加虚拟节点
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int size = grtHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(size,serviceMetaInfo);
            }
        }

        // 获取请求参数的hash值
        int hash = grtHash(requestParams);

        // 找到落在该hash值范围内的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null){
            // 如果没有找到，则返回第一个节点
            entry = virtualNodes.firstEntry();
        }
        // 返回节点信息
        return entry.getValue();
    }


    /**
     * 简单hash算法
     * @param key 对象
     * @return hash值
     */
    private int grtHash(Object key) {
        return key.hashCode();
    }
}
