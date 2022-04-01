package com.atguigu.rabbitmq.controller;

import com.atguigu.rabbitmq.config.PriorityConfig;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dai
 * @create 2022-01-2022/1/11  11-35-05
 */
@RestController
@Slf4j
public class PriorityMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/priority")
    public void sendMsg(){
        //发送消息
        for (int i = 1; i < 11; i++) {
            String msg = "info_" + i;
            if (i == 5){
                String msg5 = "优先级发送的消息——————info_5";
                rabbitTemplate.convertAndSend(PriorityConfig.EXCHANGE,"hello",msg5,message -> {
                    message.getMessageProperties().setPriority(100);
                    return message;
                });
            }else {
                rabbitTemplate.convertAndSend(PriorityConfig.EXCHANGE,"hello",msg);
            }
        }
    }
}
