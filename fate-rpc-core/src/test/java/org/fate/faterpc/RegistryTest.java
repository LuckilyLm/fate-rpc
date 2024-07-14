package org.fate.faterpc;

import org.fate.faterpc.config.RegistryConfig;
import org.fate.faterpc.model.ServiceMetaInfo;
import org.fate.faterpc.registry.EtcdRegistry;
import org.fate.faterpc.registry.Registry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Author: Fate
 * @Date: 2024/7/11 22:14
 **/
public class RegistryTest
{
    final Registry registry = new EtcdRegistry();

    @Before
    public void init(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://locahost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void  register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("FateService");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServicePort(1245);
        registry.register(serviceMetaInfo);
    }

    @Test
    public void unregister() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("org.fate.example.common.service.UserService");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServicePort(8080);
        registry.unregister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("org.fate.example.common.service.UserService");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServicePort(8080);
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfos);
    }

}
