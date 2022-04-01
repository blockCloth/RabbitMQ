package org.atguigu.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.atguigu.util.RabbitMQUtils;

/**
 * @author dai
 * @create 2021-12-2021/12/25  13-05-33
 */
public class ConsoleInfo {
    //定义队列名
    public static final String QUEUE_NAME = "console";
    //定义交换机名称
    public static final String EXCHANGE_NAME = "direct_log";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建队列
        chanel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定交换机
        chanel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"info");
        //接受消息
        System.out.println("console(info)队列等待消息中...");

        //成功消息回到
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("console获取到的消息：" + new String(message.getBody()));
        };

        chanel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
