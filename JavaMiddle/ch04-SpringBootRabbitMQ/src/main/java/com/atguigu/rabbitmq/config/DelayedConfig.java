package com.atguigu.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dai
 * @create 2022-01-2022/1/10  15-56-43
 */
@Configuration
public class DelayedConfig {
    //创建延迟交换机
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    //创建普通队列
    public static final String DELAYED_QUEUE = "delayed.queue";
    //创建routing-key
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //创建延迟交换机，返回值是一个自定义交换机类型
    @Bean
    public CustomExchange delayedExcepion(){
        Map<String, Object> arguments = new HashMap<>();
        //添加一个直接类型的延迟交换机
        arguments.put("x-delayed-type","direct");

        /**
         * 1、交换机名称
         * 2、交换机类型
         * 3、交换机是否持久化
         * 4、用完是否自动删除
         * 4、其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE,"x-delayed-message",false,false,arguments);
    }

    //创建交换机
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE);
    }

    //绑定交换机
    @Bean
    public Binding queueBindException(@Qualifier("delayedQueue") Queue queue,
                                      @Qualifier("delayedExcepion") CustomExchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
