package org.fate.faterpc.proxy;

import org.fate.faterpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @Author: Fate
 * @Date: 2024/7/11 15:39
 **/

@SuppressWarnings("unchecked")
public class ServiceProxyFactory
{

    /**
     * 根据配置是否使用mock代理
     * @param serviceClass 服务接口类
     * @return 对应代理
     * @param <T> 代理对象
     */
    public static <T> T getProxy(Class<T> serviceClass){
        if(RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new ServiceProxy());
    }

    /**
     * 根据服务类型获取Mock代理对象
     * @param serviceClass 服务接口类
     * @return 对应Mock代理对象
     * @param <T> 对应服务接口的类型
     */
    public static <T> T getMockProxy(Class<T> serviceClass)
    {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new MockServiceProxy());
    }
}
