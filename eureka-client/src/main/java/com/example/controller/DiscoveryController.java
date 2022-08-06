package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务发现
 */
@RestController
public class DiscoveryController {

    @Autowired
    private DiscoveryClient discoveryClient;

    //服务发现:通过服务的应用名称找到服务的具体实例的过程
    @GetMapping("/test")
    public String doDiscovery(String serviceName){
        //这里去找  eurekaClient客户端2的IP和port

        //通过服务的应用名 找到服务的具体信息
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        instances.forEach(System.out::println);

        String ip = instances.get(0).getHost();

        int port = instances.get(0).getPort();

        System.out.println(ip+port+"");
        return instances.get(0).toString();
    }
}
