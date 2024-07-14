package org.fate.faterpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.fate.faterpc.RpcApplication;
import org.fate.faterpc.constant.ProtocolConstant;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.model.ServiceMetaInfo;
import org.fate.faterpc.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Fate
 * @Date: 2024/7/12 21:37
 **/
public class VertxTcpClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxTcpClient.class);

    /**
     * 发送tcp请求
     * @param rpcRequest 请求参数对象
     * @param firstServiceMetaInfo 服务元信息
     * @return 响应对象
     * @throws ExecutionException 执行异常
     * @throws InterruptedException 中断异常
     */
    @SuppressWarnings("unchecked")
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo firstServiceMetaInfo) throws ExecutionException, InterruptedException {
        // 发送tcp请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(firstServiceMetaInfo.getServicePort(), firstServiceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        LOGGER.info("连接到服务提供方:{}", firstServiceMetaInfo.getServiceAddress());
                        NetSocket socket = result.result();

                        // 发送数据
                        // 构造自定义tcp协议消息
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();

                        // 设置协议头
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte) Objects.requireNonNull(ProtocolMessageSerializerEnum
                                .getEnumByValue(RpcApplication
                                        .getRpcConfig()
                                        .getSerializer())).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        // 随机生成请求id
                        header.setRequestId(IdUtil.getSnowflakeNextId());

                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);
                        // 编码消息
                        try {
                            Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                            // 发送消息
                            socket.write(encode);
                        } catch (Exception e) {
                            throw new RuntimeException("编码消息失败", e);
                        }
                        // 接收响应
                        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                                buffer -> {
                                    try {
                                        ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                                (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                        responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                    } catch (Exception e) {
                                        throw new RuntimeException("协议消息解码错误");
                                    }
                                });
                        socket.handler(bufferHandlerWrapper);
                    }else {
                        LOGGER.error("连接服务提供方失败:{}", firstServiceMetaInfo.getServiceAddress());
                    }

                });
        // 阻塞等待响应
        RpcResponse rpcResponse = responseFuture.get();
        // 关闭连接
        netClient.close();
        return rpcResponse;
    }

}
