package org.fate.faterpc.protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举类
 * @Author: Fate
 * @Date: 2024/7/12 21:11
 **/
@Getter
public enum ProtocolMessageStatusEnum
{
    OK("OK", 20),
    BAD_REQUEST("Bad Request", 40),
    BAD_RESPONSE("Bad Response", 50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举对象
     * @param value 值
     * @return 枚举对象
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum e : ProtocolMessageStatusEnum.values()) {
            if(e.value == value){
                return e;
            }
        }
        return null;
    }
}
