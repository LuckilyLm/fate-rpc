package org.fate.examplespringbootconsumer;

import org.fate.example.common.model.User;
import org.fate.example.common.service.ImageService;
import org.fate.example.common.service.UserService;
import org.fate.faterpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * @Author: Fate
 * @Date: 2024/7/13 19:29
 **/

@Service
public class ExampleServiceImpl {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcReference
    private UserService userService;

    @RpcReference
    private ImageService imageService;

    /**
     * 测试方法
     */
    public void test() {
        User user = new User("Fate");
        User resultUser = userService.getUser(user);
        String upload = imageService.upload("test.jpg");
        System.out.println(upload);
        System.out.println(resultUser.getName());
    }

}