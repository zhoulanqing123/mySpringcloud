package com.example.listener;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class topicListener {


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic_queue",durable = "true"),
            exchange = @Exchange(value = "topic_exchange",type = "topic",durable = "true")))
    public void topicMsg(Message message, AMQP.Channel channel){
        String data = message.getBody()+"";
    }
}
