package org.fate.example.common.service;

import org.fate.example.common.model.User;

/**
 * @Author: Fate
 * @Date: 2024/7/10 22:50
 **/
public interface UserService
{
    /**
     * 获取用户
     * @param user 用户对象
     * @return 用户对象
     */
    User getUser(User user);
}
