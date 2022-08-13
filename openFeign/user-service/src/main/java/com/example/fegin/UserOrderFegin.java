package com.example.fegin;

import com.example.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 服务提供者的应用名称
 */
@FeignClient("order-service")
public interface UserOrderFegin {

    /**
     * 需要调哪个接口 就写他的方法签名
     * 方法签名（一个方法的所有属性）
     * @return
     */
    @GetMapping("doOrder")
    public String doOrder();

    @GetMapping("testUrl/{name}/and/{age}")
    public String testUrl(@PathVariable("name")String name, @PathVariable("age")Integer age);

    @GetMapping("oneParam")
    public String oneParam(@RequestParam(required = false) String name);

    @GetMapping("twoParam")
    public String twoParam(@RequestParam(required = false) String name,@RequestParam(required = false) Integer age);

    @PostMapping("oneObj")
    public String oneObj(@RequestBody Order order);

    @PostMapping("oneObjOneParm")
    public String oneObjOneParm(@RequestBody Order order,@RequestParam("name") String name);

    @GetMapping
    public String testTime(@RequestParam Date date);

}
