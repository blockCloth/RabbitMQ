package com.atguigu.basicAck;

import com.atguigu.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/16  11-14-05
 */
public class ProducerAck {
    public static final String TASK_QUEUE_NAME = "ack_task";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建队列
        //将队列进行持久化
        boolean durable = true;
        chanel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //将消息标记为持久化(MessageProperties.PERSISTENT_TEXT_PLAIN，表示将消息持久化)
            chanel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("消息发送完成");
        }
    }
}
