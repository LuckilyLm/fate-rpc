package org.fate.faterpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.protocol.ProtocolMessage;
import org.fate.faterpc.protocol.ProtocolMessageDecoder;
import org.fate.faterpc.protocol.ProtocolMessageEncoder;
import org.fate.faterpc.protocol.ProtocolMessageTypeEnum;
import org.fate.faterpc.registry.LocalRegistry;

import java.lang.reflect.Method;

/**
 * TcpServerHandler
 * @Author: Fate
 * @Date: 2024/7/12 22:53
 **/
@Slf4j
public class TcpServerHandler implements Handler<NetSocket> {

    @Override
    @SuppressWarnings("unchecked")
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接受请求 解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            }catch (Exception e){
                // 解码失败
                throw new RuntimeException("协议解析失败", e);
            }
            RpcRequest rpcRequest = protocolMessage.getBody();

            // 处理请求
            // 构造结果响应对象
            RpcResponse rpcResponse = new RpcResponse();
            try {
                // 获取要调用的服务实现类,通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass. getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                // 封装结果响应对象
                rpcResponse.setMessage("success");
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                // 处理请求失败
                log.error("处理请求失败", e);
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 封装响应对象
            // 编码响应对象
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte)ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (Exception e) {
                throw new RuntimeException("协议编码失败",e);
            }
        });
        netSocket.handler(tcpBufferHandlerWrapper);
    }
}
