package org.fate.faterpc.bootstrap;

import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.config.RpcConfig;
import org.fate.faterpc.model.ServiceMetaInfo;
import org.fate.faterpc.model.ServiceRegisterInfo;
import org.fate.faterpc.registry.LocalRegistry;
import org.fate.faterpc.registry.Registry;
import org.fate.faterpc.registry.RegistryFactory;
import org.fate.faterpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @Author: Fate
 * @Date: 2024/7/13 17:00
 **/
public class ProviderBootstrap {

    /**
     * 初始化
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();

            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        // 启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
