package com.example.model;


import com.example.model.FishStatus;
import lombok.Data;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 断路器的模型
 *
 */

@Data
public class Fish {

    /**
     * 窗口时间
     */
    public static  final Integer WINDOW_TIME = 20;

    /**
     * 最大失败次数
     */
    public static final Integer MAX_FAIL_COUNT = 3;

    /**
     * 断路器的状态
     */
    private FishStatus status = FishStatus.CLOSE;

    /**
     * 当前断路器失败次数
     */
    private AtomicInteger currentFailCount = new AtomicInteger(0);

    /**
     * 线程池
     */
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            4,
            8,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(20),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()

    );

    private Object lock = new Object();


    {

        poolExecutor.execute(()->{
            //定期删除  滑动窗口
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(WINDOW_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //如果断路器是开的 不会去调用 就不会失败 不计失败次数 没必要清零
                if(this.status.equals(FishStatus.CLOSE)){
                    //失败记次数
                    this.currentFailCount.set(0);
                }else {
                    synchronized (lock){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 记录失败次数
     */
    public void addFailCount() {
        //++i
        int i = currentFailCount.incrementAndGet();
        if(i>=MAX_FAIL_COUNT){
            //失败次数到阈值
            //修改断路器状态为打开
            this.status=FishStatus.OPEN;
            //当断路器打开以后 不能方法  需要将1断路器变成半开

            //等待一个时间窗口 让断路器变成半开
            poolExecutor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(WINDOW_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.status = FishStatus.HALF_OPEN;
                //重置失败次数 不重置下次进来立刻失败
                this.currentFailCount.set(0);
            });
        }
    }
}
