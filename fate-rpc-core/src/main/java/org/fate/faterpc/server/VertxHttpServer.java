package org.fate.faterpc.server;

import io.vertx.core.Vertx;

/**
 * @Author: Fate
 * @Date: 2024/7/10 23:08
 **/
public class VertxHttpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        // 创建vertx实例
        Vertx vertx = Vertx.vertx();
        // 创建http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        // 监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器并监听端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + port);
            } else {
                System.out.println("HTTP server start failed: " + result.cause());
            }
        });
    }
}
