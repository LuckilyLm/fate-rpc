package org.fate.faterpc.protocol;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.constant.ProtocolConstant;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.model.RpcResponse;
import org.fate.faterpc.serializer.Serializer;
import org.fate.faterpc.serializer.SerializerFactory;

/**
 * 协议消息解码器
 * @Author: Fate
 * @Date: 2024/7/12 22:27
 **/
@Slf4j
public class ProtocolMessageDecoder
{
    /**
     * 解码消息
     * @param buffer 消息缓冲区
     * @return 解码后的协议消息
     * @throws Exception 解码异常
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws Exception {

        ProtocolMessage.Header header = new ProtocolMessage.Header();
        // 分别从指定位置读取 buffer 中的 magic、version、type、bodyLength等字段
        byte magic = buffer.getByte(0);
        // 校验 magic 字段
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new IllegalArgumentException("非法消息magic:" + magic);
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        // 解决粘包问题，只读取指定长度的部分
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        // 反序列化消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new IllegalArgumentException("非法消息序列化器:" + header.getSerializer());
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByValue(header.getType());
        if (messageTypeEnum == null) {
            throw new IllegalArgumentException("非法消息类型:" + header.getType());
        }

        switch (messageTypeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            case OTHERS:
            case HEART_BEAT:
            default:
                throw new IllegalArgumentException("暂不支持的消息类型:" + header.getType());
        }

    }
}
