package org.fate.example.provider;

import org.fate.example.common.service.UserService;
import org.fate.faterpc.bootstrap.ProviderBootstrap;
import org.fate.faterpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 简单提供者示例
 * @Author: Fate
 * @Date: 2024/7/10 22:58
 **/
public class EasyProviderExample {
    public static void main(String[] args) {

        // 注册服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserServiceImpl> userServiceServiceRegisterInfo =
                new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(userServiceServiceRegisterInfo);

        // 启动提供者
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
