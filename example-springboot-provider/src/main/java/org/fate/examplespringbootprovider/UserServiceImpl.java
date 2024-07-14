package org.fate.examplespringbootprovider;

import org.fate.example.common.model.User;
import org.fate.example.common.service.UserService;
import org.fate.faterpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @Author: Fate
 * @Date: 2024/7/13 18:14
 **/

@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}