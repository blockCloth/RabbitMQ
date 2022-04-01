package com.atguigu;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/11  19-37-38
 */
public class Producer {
    //定义队列名
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接配置
        factory.setHost("192.168.75.130");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 创建一个队列
         * String var1, boolean var2, boolean var3, boolean var4, Map<String, Object> var5
         * 1、队列名称
         * 2、队列中的数据是否需要持久化，默认为存储到磁盘中的
         * 3、该队列是否支持多个消费者使用，true为确认多个消费者可以使用
         * 4、是否自动删除，当最后一个消费者连接完毕之后是否删除，true为删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,true,null);
        //需要发送的消息
        String message = "Hello World";
        /**
         * 需要发送的消息
         * 1、需要发送到的交换机
         * 2、路由的key是哪个
         * 3、其他的参数信息
         * 4、发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕！");
    }
}
