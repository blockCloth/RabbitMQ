package com.atguigu.rabbitmq.controller;

import com.atguigu.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dai
 * @create 2022-01-2022/1/10  19-51-32
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        //设置消息id
        CorrelationData correlationData = null;
        correlationData = new CorrelationData("1");

        log.info(correlationData.getId());
        //发送消息
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                ConfirmConfig.CONFIRM_ROUTING_KEY,message + "1",correlationData);
        log.info("当前时间：{}，发送一条消息给确认交换机：{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),
                message);

        //设置id为2的消息
        correlationData = new CorrelationData("2");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                //将消息2的routing-key设置成错误的
                ConfirmConfig.CONFIRM_ROUTING_KEY + "11",message + "2",correlationData);
        log.info("当前时间：{}，发送一条消息给确认交换机：{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),
                message
        );
    }
}
