package org.fate.example.proxy;

import java.lang.reflect.Proxy;

/**
 * @Description: 服务代理工厂（用于创建代理对象）
 * @Author: Fate
 * @Date: 2024/7/11 0:23
 **/

public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass 服务接口
     * @return  对应的代理对象
     * @param <T> 服务接口类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }
}
