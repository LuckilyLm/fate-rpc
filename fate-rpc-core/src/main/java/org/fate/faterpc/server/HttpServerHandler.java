package org.fate.faterpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.registry.LocalRegistry;
import org.fate.faterpc.serializer.Serializer;
import org.fate.faterpc.serializer.SerializerFactory;

import java.lang.reflect.Method;

/**
 * @Description: Http 请求处理器
 * @Author: Fate
 * @Date: 2024/7/10 23:39
 **/

@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 记录日志
        System.out.println("receive request from " + request.method() + " " + request.uri());
        // 异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;

            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                log.error("Exception", e);
            }

            // 处理RPC请求
            RpcResponse rpcResponse = new RpcResponse();

            if (rpcRequest == null){
                rpcResponse.setMessage("RpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return ;
            }

            try {
                // 获取要调用的服务实现类 通过发射机制获取服务实现类并调用方法
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());

                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("success");
            }catch (Exception e){
                log.error("Exception", e);
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 异步响应RPC请求
            doResponse(request, rpcResponse, serializer);
        });
    }

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("Content-Type", "application/json");

        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            log.error("Exception", e);
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
