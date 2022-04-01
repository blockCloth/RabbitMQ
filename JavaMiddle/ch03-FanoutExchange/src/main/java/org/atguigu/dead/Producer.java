package org.atguigu.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.atguigu.util.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/6  14-30-11
 */
public class Producer {
    //定义普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //定义普通队列
    public static final String NORMAL_QUEUE = "normal_queue";

    //发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建队列
        chanel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //绑定队列
        chanel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");

        //设置生产者发送的消息10s之后过期,设置最大长度，取消消息过期时间
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        //发送消息
        for (int i = 1; i < 11; i++) {
            String message = "info_" + i;
            System.out.println("生产者发送消息：" + message);
            //发送消息
            chanel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
