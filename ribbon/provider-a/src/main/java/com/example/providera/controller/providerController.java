package com.example.providera.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class providerController {

    @GetMapping("hello")
    public String hello(){
        return "我是提供者aaaa的接口";
    }
}
