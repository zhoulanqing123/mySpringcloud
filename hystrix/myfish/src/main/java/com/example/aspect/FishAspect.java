package com.example.aspect;


import com.example.model.Fish;
import com.example.model.FishStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@Aspect
public class FishAspect {

//    public static  final String POINT_CUT = "execution (* com.example.controller.FishController.doRpc(..))";

    //每个提供者都有自己的断路器
    //在消费者里去创建一个断路器的容器
    public static Map<String, Fish> fishMap = new HashMap<>();

    static {
        //假设 是需要去调用order-service的服务
        fishMap.put("order-service",new Fish());

    }

    Random random = new Random();

    /**
     * 类比拦截器
     * 判断 当前断路器的状态 从而决定是否发起调用（执行目标方法）
     * @param joinPoint
     * @return
     */
    @Around(value ="@annotation(com.example.anno.MyFish)")
    public Object fishAround(ProceedingJoinPoint joinPoint){
        Object result = null;
        //获取到当前提供者的断路器
       Fish fish  = fishMap.get("order-service");
        FishStatus status = fish.getStatus();
        switch (status){
            case CLOSE:
                //正常 去调用 执行目标方法
                try {
                    result  = joinPoint.proceed();
                    return result;
                } catch (Throwable throwable) {
                    //调用失败 记录当前断路器失败次数
                    fish.addFailCount();
                    return "失败！！！";
                }
            case OPEN:
                //不能调用
                return "失败!!!";
            case HALF_OPEN:
                //用少许流量去调用
                int i = random.nextInt(5);
                if(i==1){
                    try {
                        result = joinPoint.proceed();
                        //调用成功 关闭断路器
                        fish.setStatus(FishStatus.CLOSE);
                        //断路器关闭 唤醒滑动窗口线程 进行清零操作
                        synchronized (fish.getLock()){
                            fish.getLock().notifyAll();
                        }
                    } catch (Throwable throwable) {
                        return "失败";
                    }
                }
                default:
                    return "失败";


        }
    }

}
