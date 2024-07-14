package org.fate.faterpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置工具类
 * @Author: Fate
 * @Date: 2024/7/11 14:28
 **/


public class ConfigUtil
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * 加载配置,默认环境
     * @param clazz 类
     * @param prefix 前缀
     * @return 配置文件
     * @param <T> 配置类
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix){
        return loadConfig(clazz, prefix, "");
    }


    /**
     * 加载配置,支持区分环境
     * @param clazz 类
     * @param prefix 前缀
     * @param environment 环境名称
     * @return 配置文件
     * @param <T> 配置类
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix,String environment){
        StringBuilder stringBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
             stringBuilder.append("-").append(environment);
        }
        stringBuilder.append(".properties");
        LOGGER.info("加载配置文件:{}", stringBuilder);
        Props props = new Props(stringBuilder.toString());
        return props.toBean(clazz, prefix);
    }
}
