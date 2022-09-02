package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class mqConfig {

    private final static String QUEUENAME = "defaultQueue";

    private final static String TTL_MESSAGE_QUEUENAME = "topic_message_ttl";

    private final static String TTL_QUEUE_QUEUENAME = "topic_queue_ttl";

    private final static String TOPIC_EXCHANGENAME = "topic_exchange";

    private final static String TOPIC_MESSAGE_EXCHANGENAME = "topic_message_exchange";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean("defaultQueue")
    public Queue testDefault(){
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);
        return new Queue(QUEUENAME,true);
    }


    @Bean
    public Queue topicQueue(){
        //1.队列名 2.是否持久化 3.是否独占 4.自动删除 5.其他参数
//        Queue queue = new Queue("topic_queue_ttl",true,false,false,null);
//        Queue queue = QueueBuilder.durable("topic_queue_ttl").ttl(10000).build();
        Map<String,Object> args = deadQueueArgs();
        Queue queue = QueueBuilder.durable(TTL_MESSAGE_QUEUENAME).withArguments(args).build();
        return queue;
    }
    @Bean
    public Queue topicTtlQueue(){
        Map<String,Object> args = deadQueueArgs();
        //1.队列名 2.是否持久化 3.是否独占 4.自动删除 5.其他参数
//        Queue queue = new Queue("topic_queue_ttl",true,false,false,null);
        Queue queue = QueueBuilder.durable(TTL_QUEUE_QUEUENAME).ttl(10000).withArguments(args).build();
        return queue;
    }


    @Bean
    public TopicExchange topicExchange(){
        //队列设置超时时间
//        Map agrs = new HashMap<>();
//        agrs.put("x-message-ttl",10000);
        //参数介绍
        //1.交换器名 2.是否持久化 3.自动删除 4.其他参数
        return new TopicExchange(TOPIC_EXCHANGENAME,true,false,null);
    }
    @Bean
    public TopicExchange topicMessageExchange(){
        //队列设置超时时间
//        Map agrs = new HashMap<>();
//        agrs.put("x-message-ttl",10000);
        //参数介绍
        //1.交换器名 2.是否持久化 3.自动删除 4.其他参数
        return new TopicExchange(TOPIC_MESSAGE_EXCHANGENAME,true,false,null);
    }

    @Bean
    public Binding bingExchange2(){
        //topic_queue_ttl队列绑定topic_exchange交换机
        return BindingBuilder.bind(topicTtlQueue())   //绑定队列
                .to(topicExchange())       //队列绑定到哪个交换器
                .with("#");         //绑定路由key,必须指定
    }
    @Bean
    public Binding bingMessageExchange(){
        //topic_message_ttl绑定topic_message_exchange
        return BindingBuilder.bind(topicQueue())   //绑定队列
                .to(topicMessageExchange())       //队列绑定到哪个交换器
                .with("#");         //绑定路由key,必须指定
    }


    @Bean
    public TopicExchange delayChange(){
        //死信交换机
        return new TopicExchange("delay_exchange",true,false);
    }

    @Bean
    public Queue delayqueue(){
        //死信队列
        return QueueBuilder.durable("delay_queue").build();
    }

    @Bean
    public Binding bingDelyExchange(){
        //delay_queue绑定到delay_exchange交换机上
        return BindingBuilder.bind(delayqueue())   //绑定队列
                .to(delayChange())       //队列绑定到哪个交换器
                .with("delay");         //绑定路由key,必须指定
    }

    /**
     * 转发到死信队列 对应交换机（delay_exchange）
     */
    private Map<String,Object> deadQueueArgs(){
        HashMap<String,Object> map = new HashMap();
        map.put("x-dead-letter-exchange","delay_exchange");
        map.put("x-dead-letter-routing-key","delay");
        return map;
    }
}
