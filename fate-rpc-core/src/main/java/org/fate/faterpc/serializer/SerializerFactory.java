package org.fate.faterpc.serializer;

import cn.hutool.core.util.StrUtil;
import org.fate.faterpc.spi.SpiLoader;


/**
 * 饿汉式单例序列化工厂类
 * @Author: Fate
 * @Date: 2024/7/11 16:55
 **/
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    public static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 根据key获取序列化器
     * @param key 序列化器key
     * @return 序列化器
     */
    public static Serializer getInstance(String key) {
        if(StrUtil.isBlank(key)){
            return DEFAULT_SERIALIZER;
        }
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
