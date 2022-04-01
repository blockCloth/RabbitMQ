package org.atguigu.dead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.atguigu.util.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/6  14-44-00
 */
public class DeadConsumer {
    //定义死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //获取消息
        System.out.println("DeadConsumer等待消息中....");
        //成功接受消息的回调
        DeliverCallback deliverCallback  = (consumerTag, message) -> {
            System.out.println("DeadConsumer接受到的消息：" + new String(message.getBody()));
        };
        //接受消息
        chanel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
