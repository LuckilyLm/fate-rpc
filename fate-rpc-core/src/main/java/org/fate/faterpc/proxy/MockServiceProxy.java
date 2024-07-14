package org.fate.faterpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理（基于JDK动态代理）
 * @Author: Fate
 * @Date: 2024/7/11 15:29
 **/
@Slf4j
public class MockServiceProxy implements InvocationHandler
{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取方法的返回值类型, 并获取默认值
        Class<?> returnType = method.getReturnType();
        log.info("MockServiceProxy invoke method: {}, returnType: {}", method.getName(), returnType.getName());
        return getDefaultValue(returnType);
    }


    /**
     * 获取方法的默认返回值
     * @param returnType 方法的返回值类型
     * @return 方法的默认返回值
     */
    private Object getDefaultValue(Class<?> returnType) {

        // 基本类型
        if(returnType.isPrimitive()){
            if(returnType == int.class){
                return 0;
            }
            else if(returnType == long.class){
                return 0L;
            }
            else if(returnType == boolean.class){
                return false;
            }
            else if(returnType == double.class){
                return 0D;
            }
            else if(returnType == float.class){
                return 0F;
            }
            else if(returnType == char.class){
                return '\0';
            }
            else if(returnType == short.class){
                return 0;
            }
            else if(returnType == byte.class){
                return 0;
            }
            else if(returnType == void.class){
                return null;
            }
        }
        // 对象类型
        return null;
    }
}
