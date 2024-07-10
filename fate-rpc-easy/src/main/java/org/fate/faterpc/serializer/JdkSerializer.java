package org.fate.faterpc.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description: jdk序列化器
 * @Author: Fate
 * @Date: 2024/7/10 23:27
 **/
public class JdkSerializer implements Serializer {

    /**
     * 序列化对象
     * @param obj 待序列化对象
     * @return 序列化后的字节数组
     * @param <T> 待序列化对象类型
     * @throws Exception 序列化异常
     */
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 反序列化字节数组
     * @param bytes 待反序列化字节数组
     * @param clazz 待反序列化对象类型
     * @return 反序列化后的对象
     * @param <T> 待反序列化对象类型
     * @throws Exception 反序列化异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            return (T) objectInputStream.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
