package org.fate.example.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.serializer.Serializer;
import org.fate.faterpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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

        // 指定序列化方式
         Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // TODO 需要使用注册中心获取服务地址解决硬编码问题
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(bodyBytes).execute())
            {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }
}
