package org.fate.faterpc.loadbalancer;

/**
 * 负载均衡器键名常量
 * @Author: Fate
 * @Date: 2024/7/13 15:33
 **/
public interface LoadBalancerKeys
{
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";
}
