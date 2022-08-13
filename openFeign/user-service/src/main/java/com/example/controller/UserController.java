package com.example.controller;

import com.example.domain.Order;
import com.example.fegin.UserOrderFegin;
import com.netflix.servo.util.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.applet.Main;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 动态代理 JDK（java inteface ）  cglib (subClass子类)
     * jdk 动态代理 只要是代理对象调用的方法必须走 InvocationHandler invoke
     */

    @Autowired
    private UserOrderFegin userOrderFegin;

    /**
     * 浏览器——————>user-service(/userDoOrder)  ------RPC(fegin)--------> order-service(/doOrder)
     * fegion的默认等待时间1s
     * 超过1s就报错 超时
     * @return
     */
    @GetMapping("userDoOrder")
    public String userDoOrder(){
        System.out.println("有用户进来了");
        //这里发起远程调用
        String s = userOrderFegin.doOrder();
        return s;
    }

    /**
     * 不建议单独传递时间参数
     * 1转成字符串
     * 2放到对象里
     * 3 jdk LocalDate LocalDateTime 会丢失s
     * @return
     */
    @GetMapping("time")
    public String time(){
        Date date = new Date();
        //年月日
        LocalDate now = LocalDate.now();
        //年月日 时分秒
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println(date);
        //这里发起远程调用
        String s = userOrderFegin.testTime(date);
        return s;
    }


    @GetMapping("testParam")
    public String testParam(){
        String zlq = userOrderFegin.testUrl("zlq", 18);
        System.out.println(zlq);
        String zlq1 = userOrderFegin.oneParam("zlq1");
        System.out.println(zlq1);
        String zlq2 = userOrderFegin.twoParam("zlq2", 18);
        System.out.println(zlq2);

        Order order = Order.builder()
                .name("zlq3")
                .id(1)
                .price(30D)
                .time(new Date()).build();

        String o = userOrderFegin.oneObj(order);
        System.out.println(o);

        String o1 = userOrderFegin.oneObjOneParm(order,"zlq4");
        System.out.println(o1);
        return "ok";
    }


//    public static void main(String[] args) {
//        //jdk 动态代理
//        UserOrderFegin o = (UserOrderFegin)Proxy.newProxyInstance(UserController.class.getClassLoader(), new Class[]{UserOrderFegin.class}, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                // 能去拿到对方的ip和port 并且拿到这个方法上面的的注解里的值（应用名称） 就可以发起远程调用
//                String name = method.getName();
//                GetMapping annotation = method.getAnnotation(GetMapping.class);
//                String[] paths = annotation.value();
//                String path = paths[0];
//
//                Class<?> aClass = method.getDeclaringClass();
//                String name1 = aClass.getName();
//                FeignClient annotation1 = aClass.getAnnotation(FeignClient.class);
//                String applicationName = annotation1.value();
//                String url = "http://"+applicationName+"/"+path;
//                String forObject = restTemplate.getForObject(url, String.class);
//                System.out.println("进入invoke!");
//                return forObject;
//            }
//        });
//        String s = o.doOrder();
//        System.out.println(s);
//    }

}
