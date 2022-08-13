package com.example.fegin.hystrix;

import com.example.fegin.CustomerRentFegin;
import org.springframework.stereotype.Component;

@Component
public class CustomerRentFeginHystrix implements CustomerRentFegin {

    /**
     * 熔断器备选方案
     * @return
     */
    @Override
    public String rent() {
        return "租车失败 备选方案";
    }
}
