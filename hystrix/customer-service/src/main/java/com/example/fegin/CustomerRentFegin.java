package com.example.fegin;

import com.example.fegin.hystrix.CustomerRentFeginHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 指定熔断实现类
 */
@FeignClient(value = "rent-car-service",fallback = CustomerRentFeginHystrix.class)
public interface CustomerRentFegin {

    @GetMapping("rent")
    public String rent();
}
