package com.example.controller;

import com.example.fegin.CustomerRentFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class CustomerController {

//    @Resource
//    private CustomerRentFegin customerRentFegin;

    @Autowired
    private CustomerRentFegin customerRentFegin;

    @GetMapping("customerRent")
    public String CustomerRent(){
        System.out.println("开始租车");
        String rent = customerRentFegin.rent();
        return rent;
    }
}
