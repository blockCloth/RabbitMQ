package org.atguigu.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.atguigu.util.RabbitMQUtils;

/**
 * @author dai
 * @create 2021-12-2021/12/24  16-52-51
 */
public class TakeQueue1 {
    //获取交换机创建的队列
    public static final String QUEUE_NAME = "queue1";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel channel = RabbitMQUtils.getChanel();
        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        System.out.println("TakeQueue1等待接受消息中....");
        //接受成功的消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("TakeQueue1接受到的消息为：" + new String(message.getBody()));
        };
        //获取发送过来的消息
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
