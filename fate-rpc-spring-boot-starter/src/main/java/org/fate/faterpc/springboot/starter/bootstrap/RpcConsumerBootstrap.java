package org.fate.faterpc.springboot.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.proxy.ServiceProxyFactory;
import org.fate.faterpc.springboot.starter.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @Author: Fate
 * @Date: 2024/7/13 17:33
 **/
@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 遍历bean的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            // 如果属性上有RpcReference注解,说明是服务
            if (rpcReference != null) {
                // 为服务生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                // 如果没有显式的指定interfaceClass的类型,则默认使用该服务的类型
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                // 获取这个服务的代理
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    // 将该服务属性声明为bean
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象bean失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}