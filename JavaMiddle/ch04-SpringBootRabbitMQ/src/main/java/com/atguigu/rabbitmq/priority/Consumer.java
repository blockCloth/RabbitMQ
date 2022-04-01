package com.atguigu.rabbitmq.priority;

import com.atguigu.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/15  21-03-19
 */
//消费优先级队列
public class Consumer {
    //创建队列
    public static final String QUEUE = "hello_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();

        //消费成功的消息回调
        DeliverCallback deliverCallback = (consumerTag,delivery) -> {
            //获取消息
            String message = new String(delivery.getBody());
            System.out.println("优先级消费：" + message);

        };

        //消费失败的消息回调
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };
        //获取消息
        chanel.basicConsume(QUEUE,true,deliverCallback,cancelCallback);
    }
}
