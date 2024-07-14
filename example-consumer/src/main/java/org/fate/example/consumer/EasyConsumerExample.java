package org.fate.example.consumer;

import org.fate.example.common.model.User;
import org.fate.example.common.service.UserService;
import org.fate.faterpc.bootstrap.ConsumerBootstrap;
import org.fate.faterpc.proxy.ServiceProxyFactory;

/**
 * @Description: 简单消费者示例
 * @Author: Fate
 * @Date: 2024/7/10 23:02
 **/
public class EasyConsumerExample
{
    public static void main(String[] args) {

        // 初始化消费者
        ConsumerBootstrap.init();

        // 设置代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User("Fate");

        User newUser = userService.getUser(user);

        if(newUser != null) {
            System.out.println(newUser.getName());
        }else {

            System.out.println("user == null");
        }

        int number = userService.getNumber();
        System.err.println(number);
    }
}
