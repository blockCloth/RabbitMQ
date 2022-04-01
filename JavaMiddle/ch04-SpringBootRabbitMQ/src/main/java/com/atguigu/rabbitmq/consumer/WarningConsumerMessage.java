package com.atguigu.rabbitmq.consumer;

import com.atguigu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author dai
 * @create 2022-01-2022/1/11  09-59-58
 */

//报警消费者
@Slf4j
@Component
public class WarningConsumerMessage {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void warningConsumer(Message message){
        String msg = new String(message.getBody());
        //输出日志
        log.info("报警发现不可路由消息：{}",msg);
    }
}
