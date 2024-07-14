# fate-rpc
基于Vertx的RPC框架，实现简单的Etcd服务注册中心、负载均衡、失败重试、容错机制、自定义传输协议、自定义序列化器等功能，并集成SpringBoot基于注解驱动快速启动项目。<br/>

<h3>1.模块划分</h3>
example-commom 公用代码模块<br/>
<br/>
过度版</br>
example-consumer 服务消费者<br/>
example-provider 服务提供者<br/>
fate-rpc-easy 简易版的RPC框架<br/>
<br/>
升级版</br>
example-springboot-consumer 基于SpringBoot框架的服务消费者<br/>
example-springboot-provider 基于SpringBoot框架的服务提供者<br/>
fate-rpc-core   Fate-RPC框架的核心<br/>
fate-rpc-springboot-starter 注解驱动的RPC框架 使用SpringBoot框架快速启动<br/>

<h3>2.基本流程</h3>
![1](https://github.com/user-attachments/assets/cf90a230-b9ea-4f10-b7e6-0c2d69521320)



<h3>3.使用</h3>
在example-common模块service层下添加需要的服务<br/>
在example-springboot-provider编写实现服务类添加<font color=Yellow>@RpcService</font>并声明为Bean<br/>
在example-springboot-consumer 使用使用<font color=Yellow>@RpcReference</font>注入服务<br/>
在fate-rpc-core 编写application.properties配置文件对RpcConfig进行配置，启动Etcd注册中心<br/>
最后启动example-springboot-provider和example-springboot-consumer的启动类，调用服务即可<br/>

<h3>4.使用到的一些技术</h3>
Vertx<br/>
Etcd<br/>
SpringBoot<br/>
Hutool工具类<br/>
lombok<br/>
logback日志<br/>
