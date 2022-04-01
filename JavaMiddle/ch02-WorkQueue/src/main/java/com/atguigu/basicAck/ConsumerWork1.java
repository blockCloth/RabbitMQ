package com.atguigu.basicAck;

import com.atguigu.util.RabbitMQUtils;
import com.atguigu.util.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/16  11-19-14
 */
public class ConsumerWork1 {
    public static final String TASK_QUEUE_NAME = "ack_task";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();

        System.out.println("C1等待消息中...");
        //获取消息
        DeliverCallback deliverCallback = (var1,var2) -> {
            SleepUtils.sleep(5);
            System.out.println("接受到消息：" + new String(var2.getBody()));
            //手动应答
            chanel.basicAck(var2.getEnvelope().getDeliveryTag(),false);
        };
        //实现不公平分发,消息接受不再是轮询接受，而是谁先处理完谁就接着处理
        //prefetchCount == 1的话，表示当前消息处理为不公平处理
        //int prefetchCount = 1;
        //chanel.basicQos(prefetchCount);

        //设置欲取值,定义欲取值的数量,表示欲取值为2
        chanel.basicQos(2);

        //是否手动应答
        boolean basicAck = false;
        chanel.basicConsume(TASK_QUEUE_NAME,basicAck,deliverCallback,(CancelCallback) ->{
            System.out.println("消息被中断");
        });
    }
}
