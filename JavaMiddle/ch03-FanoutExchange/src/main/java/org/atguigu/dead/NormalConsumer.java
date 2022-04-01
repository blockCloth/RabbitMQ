package org.atguigu.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.atguigu.util.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/6  14-23-25
 */
public class NormalConsumer {
    //定义普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    //定义死信队列
    public static final String DEAD_QUEUE = "dead_queue";
    //定义死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建死信交换机
        chanel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //定义正常队列绑定死信队列的信息
        Map<String, Object> arguments = new HashMap<>();
        //绑定死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信队列的routing-key
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置普通队列的最大长度，测试拒绝消息，取消最大长度
        //arguments.put("x-max-length",6);

        //创建队列以及交换机
        chanel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        chanel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定死信队列
        chanel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("NormalConsumer等待消息中....");
        //成功接受消息的回调
        DeliverCallback deliverCallback  = (consumerTag,message) -> {
            String msg  = new String(message.getBody());
            //判断接受到的数据是否为info_6,如果是的话就拒绝该消息
            if ("info_6".equals(msg)){
                System.out.println("NormalConsumer接受到的消息：" + msg + "，此消息被拒绝接受！");
                chanel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("NormalConsumer接受到的消息：" + msg);
                chanel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        //接受消息,拒绝消息需要手动应答
        chanel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}
