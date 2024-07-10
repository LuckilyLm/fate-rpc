package org.fate.example.provider;

import org.fate.example.common.model.User;
import org.fate.example.common.service.UserService;

/**
 * @Description: 用户服务实现类
 * @Author: Fate
 * @Date: 2024/7/10 22:57
 **/
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
