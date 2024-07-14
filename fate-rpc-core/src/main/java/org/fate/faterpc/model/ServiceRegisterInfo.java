package org.fate.faterpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息
 * @Author: Fate
 * @Date: 2024/7/13 16:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T>
{
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务实现类
     */
    private Class<? extends T> implClass;
}
