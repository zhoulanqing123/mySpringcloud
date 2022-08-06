package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConusmerController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * ribbon 是如何将 http://provider/hello 路径请求成功的
     * 1.拦截这个请求
     * 2.截取主机名称
     * 3.借助eureka来做服务发现List<>
     * 4.用过负载均衡算法 拿到一个服务 ip port
     * 5.reConstructURL   重构地址
     * 6.发起请求
     * @param serviceName
     * @return
     */

    @GetMapping("testRibbon")
    public String testRibbon(String serviceName){
        String result = restTemplate.getForObject("http://"+serviceName+"/hello",String.class);
        return result;
    }
}
