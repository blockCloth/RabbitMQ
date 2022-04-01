package org.atguigu.fanout;

import com.rabbitmq.client.Channel;
import org.atguigu.util.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author dai
 * @create 2021-12-2021/12/24  16-42-24
 */
public class EmitLogsExchange {
    //定义交换机的名称
    public static final String EXCHANGE_NAME = "logs";
    //定义队列1
    public static final String QUEUE_NAME1 = "queue1";
    //定义队列1
    public static final String QUEUE_NAME2 = "queue2";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建交换机
        chanel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //绑定两个队列
        chanel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"");
        chanel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"");

        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            //获取消息
            String message = scanner.next();
            System.out.println("消息发送成功：" + message);
            //发送消息
            chanel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
