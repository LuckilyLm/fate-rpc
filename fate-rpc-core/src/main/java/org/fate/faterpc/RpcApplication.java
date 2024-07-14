package org.fate.faterpc;

import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.config.RpcConfig;
import org.fate.faterpc.constant.RpcConstant;
import org.fate.faterpc.registry.Registry;
import org.fate.faterpc.registry.RegistryFactory;
import org.fate.faterpc.utils.ConfigUtil;


/**
 * Rpc框架应用
 * 相当于holder,存放了项目全局的配置信息。使用双检锁单例模式实现。
 * @Author: Fate
 * @Date: 2024/7/11 14:38
 **/

@Slf4j
public class RpcApplication
{
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化 支持自定义配置
     * @param newRpcConfig RpcConfig实例
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init, rpcConfig: {}", rpcConfig);

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        registry.init(registryConfig);
        log.info("registry init, registryConfig: {}", registryConfig);

        // 创建并注册 ShutDown Hook, Jvm退出时关闭注册中心连接
        // Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }


    /**
     * 框架初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;

        try {
            newRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取RpcConfig实例
     * @return RpcConfig实例
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
