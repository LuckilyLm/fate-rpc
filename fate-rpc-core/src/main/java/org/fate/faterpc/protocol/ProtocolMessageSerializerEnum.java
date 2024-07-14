package org.fate.faterpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 协议消息序列化器枚举
 * @Author: Fate
 * @Date: 2024/7/12 21:21
 **/

@Getter
public enum ProtocolMessageSerializerEnum
{
    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian"),;
    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取所有枚举值
     * @return 枚举值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(ProtocolMessageSerializerEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据key获取枚举值
     * @param key 枚举key
     * @return 枚举值
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum e : ProtocolMessageSerializerEnum.values()) {
            if(e.getKey() == key){
                return e;
            }
        }
        return null;
    }


    /**
     * 根据key获取枚举值
     * @param value 枚举值
     * @return 枚举值
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        for (ProtocolMessageSerializerEnum e : ProtocolMessageSerializerEnum.values()) {
            if(e.value.equals(value)){
                return e;
            }
        }
        return null;
    }
}
