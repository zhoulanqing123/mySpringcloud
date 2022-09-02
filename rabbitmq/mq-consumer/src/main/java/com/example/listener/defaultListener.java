package com.example.listener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue("simple_queue"))
public class defaultListener {

    @RabbitHandler
    public void receive(String message){
        System.out.println(message);
    }
}
