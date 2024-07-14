# fate-rpc
基于vertx的RPC框架，实现简单的Etcd服务注册中心、负载均衡、失败重试、容错机制、自定义传输协议、自定义序列化器等功能，并集成SpringBoot基于注解驱动快速启动项目。

1.模块划分
example-commom 公用代码模块

前期开发过度版
example-consumer 服务消费者
example-provider 服务提供者
fate-rpc-easy 简易版的RPC框架

后期核心版
example-springboot-consumer 基于SpringBoot框架的服务消费者
example-springboot-provider 基于SpringBoot框架的服务提供者
fate-rpc-core   Fate-RPC框架的核心
fate-rpc-springboot-starter 注解驱动的RPC框架 使用SpringBoot框架快速启动

2.基本流程
![image](https://github.com/user-attachments/assets/f375e2f2-4043-4f52-ad7c-541c84693c47)

3.使用
在example-common模块service层下添加需要的服务
在example-springboot-provider编写实现服务类添加@RpcService并声明为Bean
在example-springboot-consumer 使用使用@RpcReference注入服务
在fate-rpc-core 编写application.properties配置文件对RpcConfig进行配置，启动Etcd注册中心
最后启动example-springboot-provider和example-springboot-consumer的启动类，调用服务即可
