package com.atguigu.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dai
 * @create 2022-01-2022/1/10  19-43-29
 */
@Configuration
public class ConfirmConfig {
    //定义确认交换机
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    //定义确认队列
    public static final String CONFIRM_QUEUE = "confirm_queue";
    //定义确认routing-key
    public static final String CONFIRM_ROUTING_KEY = "key1";
    //添加备份交换机
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    //添加备份队列
    public static final String BACKUP_QUEUE = "backup_queue";
    //添加警告交换机
    public static final String WARNING_QUEUE = "warning_queue";


    //创建交换机
    @Bean
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).alternate(BACKUP_EXCHANGE).build();
    }

    //创建队列
    @Bean
    public Queue confirmQueue(){
        return new Queue(CONFIRM_QUEUE);
    }

    //绑定交换机
    @Bean
    public Binding queueBindExchange(@Qualifier("confirmQueue") Queue queue,
                                     @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    //创建备份交换机
    @Bean
    public FanoutExchange backupExchange(){

        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //创建备份队列
    @Bean
    public Queue backupQueue(){
        return new Queue(BACKUP_QUEUE);
    }

    //创建警告队列
    @Bean
    public Queue warningQueue(){
        return new Queue(WARNING_QUEUE);
    }

    //绑定备份队列
    @Bean
    public Binding backupQueueBindExchange(@Qualifier("backupQueue") Queue queue,
                                           @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    //绑定警告队列
    @Bean
    public Binding warningQueueBindExchange(@Qualifier("warningQueue") Queue queue,
                                           @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

}
