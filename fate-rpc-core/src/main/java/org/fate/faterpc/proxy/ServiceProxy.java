package org.fate.faterpc.proxy;

import cn.hutool.core.collection.CollUtil;
import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.config.RpcConfig;
import org.fate.faterpc.constant.RpcConstant;
import org.fate.faterpc.fault.retry.RetryStrategy;
import org.fate.faterpc.fault.retry.RetryStrategyFactory;
import org.fate.faterpc.fault.tolerant.TolerantStrategy;
import org.fate.faterpc.fault.tolerant.TolerantStrategyFactory;
import org.fate.faterpc.loadbalancer.LoadBalancer;
import org.fate.faterpc.loadbalancer.LoadBalancerFactory;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.model.ServiceMetaInfo;
import org.fate.faterpc.registry.Registry;
import org.fate.faterpc.registry.RegistryFactory;
import org.fate.faterpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Description: 动态服务代理（JDK 动态代理）
 * @Author: Fate
 * @Date: 2024/7/11 0:10
 **/

public class ServiceProxy implements InvocationHandler {

    private final static Logger LOGGER = Logger.getLogger(ServiceProxy.class.getName());

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 要代理对象的全类名
        String serviceName = method.getDeclaringClass().getName();
        // 构造发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                // 服务名
                .serviceName(serviceName)
                // 要代理的方法
                .methodName(method.getName())
                // 代理方法的参数类型
                .parameterTypes(method.getParameterTypes())
                // 参数值
                .args(args)
                .build();

        // 从注册中心获取服务地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistryType());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务提供");
        }

        RpcResponse rpcResponse;
        try {

            // 负载均衡
            LoadBalancer instance = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡计算hash值的参数
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            // 选择服务
            ServiceMetaInfo slectedServiceMetaInfo = instance.select(requestParams,serviceMetaInfoList);

            // 重试策略
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
             rpcResponse = retryStrategy.doRetry(() ->
                     // 调用客户端发送tcp请求
                    VertxTcpClient.doRequest(rpcRequest, slectedServiceMetaInfo)
            );
        }catch (Exception e){
            // 容错策略
            TolerantStrategy instance = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = instance.doTolerant(null, e);
            LOGGER.severe("调用服务失败:"+e.getMessage());
        }
        return rpcResponse.getData();
    }
}
