package org.fate.faterpc.server;

/**
 * @Description: HttpServer接口
 * @Author: Fate
 * @Date: 2024/7/10 23:06
 **/
public interface HttpServer
{
    /**
     * 启动HTTP服务
     * @param port HTTP服务端口
     */
    void doStart(int port);
}
