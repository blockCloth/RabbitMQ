package com.atguigu.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dai
 * @create 2022-01-2022/1/11  11-25-28
 */
@Slf4j
@Component
public class PriorityConfig {
    //创建交换机
    public static final String EXCHANGE = "hello_exchange";
    //创建队列
    public static final String QUEUE = "hello_queue";

    //创建交换机
    @Bean
    public DirectExchange exchange(){
        return ExchangeBuilder.directExchange(EXCHANGE).build();
    }

    //创建优先级队列
    @Bean
    public Queue queue(){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);

        return new Queue(QUEUE,false,false,false,arguments);
    }

    //绑定交换机
    @Bean
    public Binding priorityQueueBindExchange(@Qualifier("queue") Queue queue,
                                     @Qualifier("exchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("hello");
    }
}
