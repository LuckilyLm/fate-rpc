package org.fate.faterpc.protocol;

import lombok.Getter;

/**
 * 协议消息类型枚举类
 * @Author: Fate
 * @Date: 2024/7/12 21:18
 **/

@Getter
public enum ProtocolMessageTypeEnum
{
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key值获取枚举值
     * @param value key值
     * @return 枚举值
     */
    public static ProtocolMessageTypeEnum getEnumByValue(int value) {
        for (ProtocolMessageTypeEnum e : ProtocolMessageTypeEnum.values()) {
            if(e.key== value){
                return e;
            }
        }
        return null;
    }
}
