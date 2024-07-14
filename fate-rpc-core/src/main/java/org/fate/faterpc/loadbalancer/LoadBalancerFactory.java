package org.fate.faterpc.loadbalancer;

import org.fate.faterpc.spi.SpiLoader;

/**
 * 负载均衡器工厂类
 * @Author: Fate
 * @Date: 2024/7/13 15:38
 **/
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 获取默认的负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取负载均衡器实例
     * @param key 负载均衡器的类型
     * @return 负载均衡器实例
     */
    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
