package org.atguigu.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.atguigu.util.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author dai
 * @create 2021-12-2021/12/25  12-55-23
 */
public class DirectLogExchange {
    //定义交换机名称
    public static final String EXCHANGE_NAME = "direct_log";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建直连交换机
        chanel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            //获取需要发送的消息
            String message = scanner.next();
            System.out.println("消息发送成功：" + message);
            //发送消息
            chanel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
