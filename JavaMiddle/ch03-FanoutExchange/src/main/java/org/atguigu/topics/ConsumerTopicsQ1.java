package org.atguigu.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.atguigu.util.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/5  15-56-57
 */
public class ConsumerTopicsQ1 {
    //定义队列名称
    public static final String QUEUE_NAME = "Q1";

    //接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取队列
        Channel chanel = RabbitMQUtils.getChanel();
        //创建队列
        chanel.queueDeclare(QUEUE_NAME,false,false,false,null);

        System.out.println("Q1等待消息中....");
        //接受成功的消息回调
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("接受到的消息：" + new String(message.getBody()) + " 绑定值为：" +
                    message.getEnvelope().getRoutingKey());
        };
        //接受消息

        chanel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
