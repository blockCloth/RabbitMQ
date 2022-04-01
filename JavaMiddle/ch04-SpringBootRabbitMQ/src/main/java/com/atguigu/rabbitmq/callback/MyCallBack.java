package com.atguigu.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @author dai
 * @create 2022-01-2022/1/10  19-55-56
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{
    //创建模板
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //注入到接口类中
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    //交换机接受消息的消息回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData!=null?correlationData.getId():"";
        //验证消息是否发送成功
        if (ack){
            log.info("交换机接受到id为：{}的消息",id);
        }else {
            log.info("交换机还未接受到id为{}的消息，原因为：{}",id,cause);
        }
    }

    //队列拒绝消息之后的消息回调
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息：{}被服务器退回，退回原因：{}，交换机：{}，路由key：{}",
                new String(returned.getMessage().getBody()),
                returned.getReplyText(),
                returned.getExchange(),
                returned.getRoutingKey());
    }
}
