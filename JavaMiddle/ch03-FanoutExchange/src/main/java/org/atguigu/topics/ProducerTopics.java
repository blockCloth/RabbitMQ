package org.atguigu.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.atguigu.util.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2022-01-2022/1/5  16-19-20
 */
public class ProducerTopics {
    //定义交换机名称
    public static final String EXCHANGE_NAME = "topics_logs";
    //定义两个队列名
    public static final String QUEUE_NAME1 = "Q1";
    public static final String QUEUE_NAME2 = "Q2";

    //发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取队列
        Channel chanel = RabbitMQUtils.getChanel();
        //创建交换机
        chanel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //绑定交换机
        chanel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"*.orange.*");
        chanel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"*.*.rabbit");
        chanel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"lazy.#");

        //发送消息
        Map<String,String> map = new HashMap<>();
        map.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        map.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        map.put("quick.orange.fox","被队列 Q1 接收到");
        map.put("lazy.brown.fox","被队列 Q2 接收到");
        map.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        map.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        map.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        map.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        //遍历发送消息
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            //获取key
            String bindKey = stringStringEntry.getKey();
            //获取value
            String message = stringStringEntry.getValue();
            //发送消息
            chanel.basicPublish(EXCHANGE_NAME,bindKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功，roundingKey：" + bindKey + "    发送值：" + message);
        }
    }
}
