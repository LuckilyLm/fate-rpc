package org.fate.faterpc.springboot.starter.annotation;

import org.fate.faterpc.constant.RpcConstant;
import org.fate.faterpc.fault.tolerant.TolerantStrategyKeys;
import org.fate.faterpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解
 * @Author: Fate
 * @Date: 2024/7/13 17:27
 **/

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     * @return 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本
     * @return 服务版本
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡策略
     * @return 负载均衡策略
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 容错策略
     * @return 容错策略
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 模拟调用
     * @return 是否模拟调用
     */
    boolean mock() default false;

}
