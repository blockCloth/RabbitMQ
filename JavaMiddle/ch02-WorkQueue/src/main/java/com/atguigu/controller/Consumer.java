package com.atguigu.controller;

import com.atguigu.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/16  10-11-34
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();

        //获取消息
        /**
         * 1、消费哪个队列
         * 2、是否自动应答
         * 3、消息接受成功回调
         * 4、消息接受失败回调
         */
        DeliverCallback deliverCallback = (consumerTag,delivery) -> {
            System.out.println("接受到的消息为：" + new String(delivery.getBody()));

        };

        CancelCallback cancelCallback = (message) ->{
            System.out.println("消息被中断...");
        };
        System.out.println("C1等待消息中...");
        chanel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
