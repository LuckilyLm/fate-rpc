package org.fate.faterpc.springboot.starter.annotation;

import org.fate.faterpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import org.fate.faterpc.springboot.starter.bootstrap.RpcInitBootstrap;
import org.fate.faterpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 开启RPC注解
 * @Author: Fate
 * @Date: 2024/7/13 17:23
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc
{
    /**
     * 是否需要启动RPC服务端
     * @return true:启动RPC服务端，false:不启动RPC服务端
     */
    boolean needServer() default true;
}

