package com.atguigu.rabbitmq.consumer;

import com.atguigu.rabbitmq.config.PriorityConfig;
import com.atguigu.rabbitmq.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author dai
 * @create 2022-01-2022/1/11  11-32-49
 */
@Slf4j
@Component
public class PriorityConsumer {

    @RabbitListener(queues = PriorityConfig.QUEUE)

    public void consumerMsg(Message message){
        //先让消息沉睡10s
//        SleepUtils.sleep(10);
        //获取消息
        String msg = new String(message.getBody());
        //输出消息
        log.info("优先级消息：" + msg);
    }
}
