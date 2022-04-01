package com.atguigu.rabbitmq.controller;

import com.atguigu.rabbitmq.config.DelayedConfig;
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
 * @create 2022-01-2022/1/9  16-36-38
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageCtonroller {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        //发送消息
        log.info("当前时间为：{}，发送一条消息给两个TTL队列{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),message);
        rabbitTemplate.convertAndSend("normal_exchange","XA","消息来自TTL为10s的队列：" + message);
        rabbitTemplate.convertAndSend("normal_exchange","XB","消息来自TTL为40s的队列：" + message);
    }

    @GetMapping("/sendMsg/{message}/{ttlTime}")
    public void sendTimeMsg(@PathVariable String message,@PathVariable String ttlTime){
        //发送消息
        rabbitTemplate.convertAndSend("normal_exchange","XC",message, CorrelationData -> {
            //在消息上设置到期时间
            CorrelationData.getMessageProperties().setExpiration(ttlTime);
            return CorrelationData;
        });
        log.info("当前时间为：{}，发送一条时长{}毫秒的消息给队列C{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),ttlTime,message);
    }

    @GetMapping("/sendDelaMsg/{message}/{ttlTime}")
    public void sendDelayedMsg(@PathVariable String message,@PathVariable Integer ttlTime){
        //发送消息
        rabbitTemplate.convertAndSend(DelayedConfig.DELAYED_EXCHANGE,
                DelayedConfig.DELAYED_ROUTING_KEY,message,msg -> {
                    //设置延迟队列消息过期时间
                     msg.getMessageProperties().setDelay(ttlTime);
                    return msg;
                });
        log.info("当前时间为：{}，发送一条时长{}毫秒的消息给队列delayed：{}",new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()),ttlTime,message);
    }
}
