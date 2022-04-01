package com.atguigu.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dai
 * @create 2022-01-2022/1/6  16-13-44
 */
@Configuration
public class TtlQueueConfig {
    //定义普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //定义死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //定义普通队列(具有TTL属性)
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //（不具有TTL属性）
    public static final String QUEUE_C = "QC";
    //定义死信队列
    public static final String DEAD_QUEUE_D = "QD";

    //创建普通交换机
    @Bean("normal_exchange")
    public DirectExchange normal_exchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    //创建死信交换机
    @Bean("dead_exchange")
    public DirectExchange dead_exchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    //创建普通队列A
    @Bean("queueA")
    public Queue queueA(){
        //创建队列属性集合
        Map<String, Object> arguments = new HashMap<>();
        //绑定死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //绑定routing-key
        arguments.put("x-dead-letter-routing-key","YD");
        //设置消息过期时间
        arguments.put("x-message-ttl",10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //创建普通队列B
    @Bean("queueB")
    public Queue queueB(){
        //创建队列属性集合
        Map<String, Object> arguments = new HashMap<>();
        //绑定死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //绑定routing-key
        arguments.put("x-dead-letter-routing-key","YD");
        //设置消息过期时间
        arguments.put("x-message-ttl",40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //创建普通队列C
    @Bean("queueC")
    public Queue queueC(){
        //创建队列属性集合
        Map<String, Object> arguments = new HashMap<>();
        //绑定死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //绑定routing-key
        arguments.put("x-dead-letter-routing-key","YD");
        //不具有TTL属性

        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //创建死信队列
    @Bean("dead_queueD")
    public Queue dead_queueD(){
        return new Queue(DEAD_QUEUE_D);
    }

    //队列A跟普通交换机绑定
    @Bean("queueABindExchange")
    public Binding queueABindExchange(@Qualifier("queueA") Queue queueA,
                                      @Qualifier("normal_exchange") DirectExchange normal_exchange){
        return BindingBuilder.bind(queueA).to(normal_exchange).with("XA");
    }

    //队列B跟普通交换机绑定
    @Bean("queueBBindExchange")
    public Binding queueBBindExchange(@Qualifier("queueB") Queue queueB,
                                      @Qualifier("normal_exchange") DirectExchange normal_exchange){
        return BindingBuilder.bind(queueB).to(normal_exchange).with("XB");
    }

    //队列C跟普通交换机绑定
    @Bean("queueCBindExchange")
    public Binding queueCBindExchange(@Qualifier("queueC") Queue queueC,
                                      @Qualifier("normal_exchange") DirectExchange normal_exchange){
        return BindingBuilder.bind(queueC).to(normal_exchange).with("XC");
    }

    //死信队列跟死信交换机绑定
    @Bean("queueD BindExchange")
    public Binding queueDBindExchange(@Qualifier("dead_queueD") Queue dead_queueD,
                                      @Qualifier("dead_exchange") DirectExchange dead_exchange){
        return BindingBuilder.bind(dead_queueD).to(dead_exchange).with("YD");
    }
}
