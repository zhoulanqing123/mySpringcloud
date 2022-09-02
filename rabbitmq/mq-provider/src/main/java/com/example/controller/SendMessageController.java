package com.example.controller;


import com.example.config.ConfirmReturnConfig;
import com.example.config.mqConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendMessageController {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Autowired
    ConfirmReturnConfig confirmReturnConfig;


    @GetMapping("send")
    public String sendMessage(){
        try {
            String queue = "simple_queue";
            String message ="简单模式mq发送";
            rabbitTemplate.convertAndSend(queue,message);
        } catch (AmqpException e) {
            e.printStackTrace();
            return "失败";
        }
        return "成功";
    }

    @GetMapping("sendDefault")
    public String sendDefault(){
        try {
            String queue = "defaultQueue";
            String message ="简单模式mq发送";
            mqConfig config = new mqConfig();
            rabbitTemplate.convertAndSend(queue,message);
        } catch (AmqpException e) {
            e.printStackTrace();
            return "失败";
        }
        return "成功";
    }

    @GetMapping("sendTopic")
    public String sendTopic(){
        try {
//            String message ="topic模式mq发送";
//            confirmReturnConfig.sendMessage("topic_exchange","zlq",message);

            for(int i=0;i<5;i++){
             if(i==3){
                 //设置过期时间
                 MessageProperties messageProperties = new MessageProperties();
                 messageProperties.setExpiration("8000");
                 String msg = "测试消息过期时间";
                 byte[] msgBytes = msg.getBytes();
                 Message message1 = new Message(msgBytes,messageProperties);
                 confirmReturnConfig.sendMessage("topic_message_exchange","zlq",message1);
             }else {
                 //不设置过期时间
                 MessageProperties messageProperties = new MessageProperties();
                 String msg = "测试消息过期时间";
                 byte[] msgBytes = msg.getBytes();
                 Message message1 = new Message(msgBytes,messageProperties);
                 confirmReturnConfig.sendMessage("topic_message_exchange","zlq",message1);
             }
            }

        } catch (AmqpException e) {
            e.printStackTrace();
            return "失败";
        }
        return "成功";
    }
}
