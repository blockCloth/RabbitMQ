package com.atguigu.confirm;

import com.atguigu.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author dai
 * @create 2021-12-2021/12/17  15-36-41
 *  消息确认测试
 */
public class ConfirmMessage {
    //定义发送消息的常量
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //单个确认
//        confirmSingle();
        //批量确认
        confirmBatch();
        //异步确认
//        ayncchronizationConfirm();
    }

    /**
     * 单个处理消息
     * @throws Exception
     */
    public static void confirmSingle() throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChanel();
        //创建队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启确认模式
        channel.confirmSelect();
        //记录开始时间
        long begin = System.currentTimeMillis();
        //发送消息
        for (int messageCount = MESSAGE_COUNT; messageCount > 0; messageCount--) {
            //获取消息
            String message = "消息_" + messageCount;
            //发送消息
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            //判断消息是否发送成功
            boolean flag = channel.waitForConfirms();
            if (flag){
                //System.out.println("消息发送成功！");
            }
        }
        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("单个发送" + MESSAGE_COUNT + "个消息，耗时" + (end-begin) + "ms");
    }

    /**
     * 批量处理消息
     * @throws Exception
     */
    public static void confirmBatch() throws Exception{
        //获取信道
        Channel chanel = RabbitMQUtils.getChanel();
        //创建信道
        String queueName = UUID.randomUUID().toString();
        chanel.queueDeclare(queueName,true,false,false,null);
        //开启确认发布
        chanel.confirmSelect();
        //计算确认发布数量
        int confirmBatchCount = 200;
        //确认发布数量
        int messageCount = 0;
        //计算发布时间
        long begin = System.currentTimeMillis();
        //发布信息
        for (int count = MESSAGE_COUNT; count > 0; count--) {
            //发送消息
            String message = "消息_" + count;
            chanel.basicPublish("",queueName,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            //对发送的消息进行自增
            messageCount++;
            //判断是否符合数量
            if (messageCount == confirmBatchCount){
                //确认发布
                chanel.waitForConfirms();
                messageCount = 0;
            }
            //再次判断是否确认发布
            if (messageCount > 200){
                chanel.waitForConfirms();
            }
        }
        //计算结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发送" + MESSAGE_COUNT + "个消息，耗时" + (end-begin) + "ms");
    }

    /**
     * 异步处理消息
     */
    public static void ayncchronizationConfirm() throws IOException, TimeoutException {
        //获取队列
        Channel chanel = RabbitMQUtils.getChanel();
        //创建信道
        String queueName = UUID.randomUUID().toString();
        chanel.queueDeclare(queueName,true,false,false,null);
        //确认发送
        chanel.confirmSelect();

        /**
         * 线程安全的一个哈希表、适用于并发的场景
         * 1、轻松将序号跟消息关联
         * 2、根据序列号删除数据
         * 3、支持高并发访问
         */
        ConcurrentSkipListMap<Long,String> skipListMap = new ConcurrentSkipListMap<>();

        /**
         * 确认发布的消息回调
         * 1、消息当前的编号
         * 2、true可以确认小于等于当前序列号的消息（表示序号小于并且等于当前序号的消息）
         *    false确认为当前消息的序号
         */
        ConfirmCallback confirmCallback = (number,mulite) -> {
            System.out.println("确认的消息：" + number);
            //验证消息是否确认，确认的话则删除
            //如果为批量处理的话，则清空
            if (mulite){
                //返回小于等于当前序号的未确认的消息
                skipListMap.headMap(number, true);
                //清空当前集合
                skipListMap.clear();
            }else {
                //清除当前序列确认的消息
                skipListMap.remove(number);
            }

        };

        /**
         * 未确认消息的回调
         */
        ConfirmCallback nconfirmCallback = (number,mulite) ->{
            //获取未确认的消息内容
            String message = skipListMap.get(number);

            System.out.println("未确认的消息：" + message + "，为确认消息的编号" + number);
        };

        /**
         * 添加一个异步确认的监听器
         * 1、确认消息的回调函函数
         * 2、未确认消息的回调函数
         */
        chanel.addConfirmListener(confirmCallback,nconfirmCallback);

        //计算发布时间
        long begin = System.currentTimeMillis();

        //循环发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            //获取需要发送的消息
            String message = "消息_" + i;
            /**
             * chanel.getNextPublishSeqNo()可以获取下一个需要发送消息的序列号
             * 获取需要发送消息的序号
             * 将编号以及消息填充到集合中
             */
            skipListMap.put(chanel.getNextPublishSeqNo(),message);
            //发送消息
            chanel.basicPublish("",queueName,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        }

        //计算结束时间
        long end = System.currentTimeMillis();
        System.out.println("异步发送" + MESSAGE_COUNT + "个消息，耗时" + (end-begin) + "ms");
    }
}
