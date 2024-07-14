package org.fate.faterpc.springboot.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.config.RpcConfig;
import org.fate.faterpc.model.ServiceMetaInfo;
import org.fate.faterpc.registry.LocalRegistry;
import org.fate.faterpc.registry.Registry;
import org.fate.faterpc.registry.RegistryFactory;
import org.fate.faterpc.springboot.starter.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Description: rpc服务提供者启动类
 * @Author: Fate
 * @Date: 2024/7/13 17:43
 **/

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注册服务
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        // 获取bean上注解
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        // 如果有注解,说明是需要注册的rpc服务
        if (rpcService != null) {
            // 1. 获取服务类
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 如果没有指定接口类,则默认使用该bean的第一个实现接口类
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            // 获取第一个实现接口类的全类名作为服务名
            String serviceName = interfaceClass.getName();
            // 获取服务版本
            String serviceVersion = rpcService.serviceVersion();

            // 2. 注册服务
            // 本地注册该服务
            LocalRegistry.register(serviceName,beanClass);

            // 获取RpcApplication的全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            // 获取本地注册中心类型并实例化
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());

            // 填充实例化服务元信息
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                // 把该服务注册到注册中心
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
