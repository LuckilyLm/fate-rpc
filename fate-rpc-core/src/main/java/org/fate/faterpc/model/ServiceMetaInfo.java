package org.fate.faterpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务元信息（注册信息）
 * @Author: Fate
 * @Date: 2024/7/11 18:11
 **/
@Data
public class ServiceMetaInfo
{
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 服务主机
     */
    private String serviceHost;

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 服务键（用于唯一标识一个服务）
     * @return 服务键
     */
    public String getServiceKey(){
        // 后续实现分组功能时，需要修改
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 服务节点键（用于唯一标识一个服务节点）
     * @return 服务节点键
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(),serviceHost, servicePort);
    }

    /**
     * 获取完整的服务地址
     * @return 完整的服务地址
     */
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost, "http")){
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
