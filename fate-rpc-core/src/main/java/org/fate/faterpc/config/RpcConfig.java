package org.fate.faterpc.config;

import lombok.Data;
import org.fate.faterpc.fault.retry.RetryStrategyKeys;
import org.fate.faterpc.fault.tolerant.TolerantStrategyKeys;
import org.fate.faterpc.loadbalancer.LoadBalancerKeys;
import org.fate.faterpc.serializer.SerializerKeys;

/**
 * @Author: Fate
 * @Date: 2024/7/11 14:26
 **/

@Data
public class RpcConfig
{

    /**
     * 注册中心配置
     */
   private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 服务名称
     */
    private String name = "fate-rpc";
    /**
     * 服务版本
     */
    private String version = "1.0";

    /**
     * 服务端主机地址
     */
    private String serverHost = "localhost";

    /**
     * 服务端端口
     */
    private Integer serverPort = 8080;

    /**
     * 序列化方式
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 负载均衡策略
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_SAFE;
}
