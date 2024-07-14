package org.fate.faterpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.fate.faterpc.serializer.Serializer;
import org.fate.faterpc.serializer.SerializerFactory;

/**
 * 协议消息编码器
 * @Author: Fate
 * @Date: 2024/7/12 21:52
 **/

public class ProtocolMessageEncoder
{
    /**
     * 编码协议消息
     * @param protocolMessage 协议消息
     * @return 缓冲区
     * @throws Exception 异常
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws Exception {
        if(protocolMessage == null || protocolMessage.getHeader() == null){
            return Buffer.buffer();
        }

        ProtocolMessage.Header header = protocolMessage.getHeader();
        Buffer buffer = Buffer.buffer();

        // 依次写入协议头信息
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new IllegalArgumentException("序列化协议错误:" + header.getSerializer());
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        // 序列化消息体
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());

        // 写入消息体长度
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}
