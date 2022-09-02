package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ConfirmReturnConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange,String routingKey,Object msg) {
        // 设置交换机处理失败消息的模式     true 表示消息由交换机 到达不了队列时，会将消息重新返回给生产者
        // 如果不设置这个指令，则交换机向队列推送消息失败后，不会触发 setReturnCallback
        rabbitTemplate.setMandatory(true);
        //消息消费者确认收到消息后，手动ack回执
        rabbitTemplate.setConfirmCallback(this);

        // return 配置
        rabbitTemplate.setReturnCallback(this);
        //发送消息
        rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }


    /**
     * 交换机并未将数据丢入指定的队列中时，触发
     *  channel.basicPublish(exchange_name,next.getKey(), true, properties,next.getValue().getBytes());
     *  参数三：true  表示如果消息无法正常投递，则return给生产者 ；false 表示直接丢弃
     * @param message   消息对象
     * @param replyCode 错误码
     * @param replyText 错误信息
     * @param exchange 交换机
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("---- returnedMessage ----replyCode="+replyCode+" replyText="+replyText+" ");
    }

    /**
     * 消息生产者发送消息至交换机时触发，用于判断交换机是否成功收到消息
     * @param correlationData  相关配置信息
     * @param ack exchange 交换机，判断交换机是否成功收到消息    true 表示交换机收到
     * @param cause  失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("---- confirm ----ack="+ack+"  cause="+String.valueOf(cause));
        log.info("correlationData -->"+correlationData.toString());
        if(ack){
            // 交换机接收到
            log.info("---- confirm ----ack==true  cause="+cause);
        }else{
            // 没有接收到
            log.info("---- confirm ----ack==false  cause="+cause);
        }
    }
}
