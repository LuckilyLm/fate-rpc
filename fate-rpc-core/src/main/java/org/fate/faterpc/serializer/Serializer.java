package org.fate.faterpc.serializer;

/**
 * @Author: Fate
 * @Date: 2024/7/10 23:26
 **/
public interface Serializer
{
    /**
     * 序列化
     * @param obj 待序列化对象
     * @return 序列化后的字节数组
     * @param <T> 待序列化对象类型
     * @throws Exception 序列化异常
     */
    <T> byte[] serialize(T obj) throws Exception;

    /**
     * 反序列化
     * @param bytes 待反序列化字节数组
     * @param clazz 待反序列化对象类型
     * @return 反序列化后的对象
     * @param <T> 待反序列化对象类型
     * @throws Exception 反序列化异常
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
