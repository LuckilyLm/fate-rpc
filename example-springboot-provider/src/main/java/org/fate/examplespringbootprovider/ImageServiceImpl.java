package org.fate.examplespringbootprovider;

import org.fate.example.common.service.ImageService;
import org.fate.faterpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @Author: Fate
 * @Date: 2024/7/14 18:33
 **/

@Service
@RpcService
public class ImageServiceImpl implements ImageService {
    @Override
    public String upload(String imagePath) {
        // TODO: 实现 上传图片到OSS
        return imagePath + "上传成功";
    }
}
