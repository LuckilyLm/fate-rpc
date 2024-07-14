package org.fate.faterpc.springboot.starter.annotation;

import org.fate.faterpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者注解 用于注册服务
 * @Author: Fate
 * @Date: 2024/7/13 17:24
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService
{
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
}
