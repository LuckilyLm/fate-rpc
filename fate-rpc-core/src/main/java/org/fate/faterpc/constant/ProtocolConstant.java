package org.fate.faterpc.constant;

/**
 * @Description: 协议常量
 * @Author: Fate
 * @Date: 2024/7/12 21:08
 **/
public interface ProtocolConstant
{
    /**
     * 协议头长度
     */
    int MESSAGE_HEADER_LENGTH =  17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本
     */
    byte PROTOCOL_VERSION = 0x1;
}
