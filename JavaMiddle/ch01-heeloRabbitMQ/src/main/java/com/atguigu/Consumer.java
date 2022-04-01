package com.atguigu;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/11  19-56-59
 */
public class Consumer {
    //需要消费的队列
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //连接信息
        factory.setHost("192.168.75.130");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        System.out.println("等待消息接受....");
        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback= (consumerTag,delivery)->{
            //获取到消息的消息体
            String message= new String(delivery.getBody());
            System.out.println(message);
        };
        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费成功之后的消息回调（获取到消息之后的消息处理）
         * 4、消费失败之后的回调（当消息被中断时的处理）
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
