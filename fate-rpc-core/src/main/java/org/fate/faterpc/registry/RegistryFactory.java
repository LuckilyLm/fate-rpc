package org.fate.faterpc.registry;

import cn.hutool.core.util.StrUtil;
import org.fate.faterpc.spi.SpiLoader;

/**
 * 注册中心工厂类（用于获取注册中心对象）
 * @Author: Fate
 * @Date: 2024/7/11 21:07
 **/
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 获取默认的注册中心对象（默认使用etcd注册中心）
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     */
    public static Registry getInstance(String key) {
        if(StrUtil.isBlank(key)){
            return DEFAULT_REGISTRY;
        }
        return SpiLoader.getInstance(Registry.class,key);
    }
}
