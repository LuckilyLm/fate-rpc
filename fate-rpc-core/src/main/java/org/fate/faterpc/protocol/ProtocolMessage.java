package org.fate.faterpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 协议消息
 * @Author: Fate
 * @Date: 2024/7/12 21:02
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T>
{
    /**
     * 协议消息头
     */
    private Header header;

    /**
     * 协议消息体
     */
    private T body;

    /**
     * 协议消息头类
     */
    @Data
    public static class Header{
        /**
         * 协议魔数
         */
        private byte magic;

        /**
         * 协议版本
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 消息类型
         */
        private byte type;

        /**
         * 消息状态
         */
        private byte status;

        /**
         * 请求ID
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}
