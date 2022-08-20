package com.example.controller;


import com.example.anno.MyFish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FishController {


    @Autowired
    RestTemplate restTemplate;



    @GetMapping("doRpc")
    @MyFish
    public String doRpc(){

        String object = restTemplate.getForObject("http://localhost:8081/abc", String.class);


        return object;
    }
}
