package com.atguigu.rabbitmq.priority;

import com.atguigu.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/11  11-22-00
 */
//生产者
public class Producter {
    //创建交换机
    public static final String EXCHANGE = "hello_exchange";
    //创建队列
    public static final String QUEUE = "hello_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建交换机以及队列
        chanel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置队列优先级属性
        Map<String, Object> argsm = new HashMap<>();
        argsm.put("x-max-priority",10);
        chanel.queueDeclare(QUEUE,false,false,false,argsm);
        //绑定交换机
        chanel.queueBind(QUEUE,EXCHANGE,"info");

        //发送消息
        for (int i = 1; i < 11; i++) {
            String msg = "info_" + i;
            //消息为5的时候优先级更高
            if (i == 5){
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                        .priority(10).build();
                //发送消息
                String primsg = "优先级消息——" + msg;
                chanel.basicPublish(EXCHANGE,"info",properties,primsg.getBytes());
            }else {
                //发送消息
                chanel.basicPublish(EXCHANGE,"info",null,msg.getBytes());
            }
        }
    }
}
