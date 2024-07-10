package org.fate.example.provider;

import org.fate.example.common.service.UserService;
import org.fate.faterpc.registy.LocalRegistry;
import org.fate.faterpc.server.HttpServer;
import org.fate.faterpc.server.VertxHttpServer;

/**
 * @Description: 简单提供者示例
 * @Author: Fate
 * @Date: 2024/7/10 22:58
 **/
public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        // 启动HTTP服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
