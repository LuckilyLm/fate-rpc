package org.fate.faterpc.config;

import lombok.Data;
import org.fate.faterpc.registry.RegistryKeys;

/**
 * RPC 框架服务注册中心配置类
 * @Author: Fate
 * @Date: 2024/7/11 18:19
 **/

@Data
public class RegistryConfig
{
    /**
     * 注册中心类型，如：zookeeper、redis、etcd、consul
     */
    private String registryType = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2379";

    /**
     * 注册中心用户名
     */
    private String username;

    /**
     * 注册中心密码
     */
    private String password;

    /**
     * 连接超时时间，单位：毫秒
     */
    private Long timeout = 10000L;
}
