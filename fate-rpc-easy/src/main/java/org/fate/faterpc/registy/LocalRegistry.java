package org.fate.faterpc.registy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 本地注册中心
 * @Author: Fate
 * @Date: 2024/7/10 23:19
 **/
public class LocalRegistry {

    /**
     * 注册信息存储
     */
    private static final ConcurrentHashMap<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 获取服务实现类
     * @param serviceName 服务名称
     * @param implClass 实现类
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务实现类
     * @param serviceName 服务名称
     * @return 实现类
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 移除服务实现类
     * @param serviceName 服务名称
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
