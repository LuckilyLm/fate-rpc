package org.fate.faterpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.server.HttpServer;
import java.nio.charset.StandardCharsets;

/**
 * @Description: tcp server based on vertx
 * @Author: Fate
 * @Date: 2024/7/12 21:29
 **/
@Slf4j
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // TODO: 根据业务需要，实现处理请求的逻辑
        System.out.println("receive request data: " + new String(requestData, StandardCharsets.UTF_8));
        // 这里简单返回一个字符串
        return "Hello,World!".getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void doStart(int port) {

        // 创建vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建tcp 服务器
        NetServer netServer = vertx.createNetServer();

        // 设置连接处理器 处理tcp连接请求
        netServer.connectHandler(new TcpServerHandler());

        // 启动TCP服务器 监听端口
        netServer.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port {}", port);
            } else {
                log.error("TCP server start failed: {}", result.cause().toString());
            }
        });
    }
}
