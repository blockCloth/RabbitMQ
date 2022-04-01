package com.atguigu.controller;

import com.atguigu.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/16  10-04-53
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建一个队列
        /**
         * 1、队列名称
         * 2、是否需要持久化
         * 3、是否支持共享
         * 4、是否自动删除
         * 5、其他
         */
        chanel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (true){
            String message = scanner.next();
            //发送消息
            /**
             * 1、需要发送到的交换机
             * 2、队列名
             * 3、其他参数
             * 4、消息体
             */
            chanel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕");
        }
    }
}
