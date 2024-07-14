package org.fate.faterpc.bootstrap;

import org.fate.faterpc.RpcApplication;

/**
 * @Author: Fate
 * @Date: 2024/7/13 17:12
 **/
public class ConsumerBootstrap
{
    public static void init(){
        // Rpc框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}
