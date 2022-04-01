package com.atguigu.rabbitmq.consumer;

import com.atguigu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dai
 * @create 2022-01-2022/1/10  19-48-44
 */
@Slf4j
@Component
public class ConfirmConsumerMessage {

    //获取消息
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void consumerMsg(Message message){
        //获取消息
        String msg = new String(message.getBody());
        //接受生产者发送过来的消息
        log.info("当前时间为：{}，接受生产者发送的消息为：{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),msg);
    }
}
