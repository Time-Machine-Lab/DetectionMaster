package com.rvc.mq.consumer.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rvc.config.RabbitmqConfig;
import com.rvc.sdk.aliyun.AliyunAudioDetection;
import com.rvc.sdk.aliyun.AliyunImageDetection;
import com.rvc.sdk.aliyun.AliyunTextDetection;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static com.rvc.constant.Constants.*;

/**
 * @ClassName ReceiveHandler
 * @Description TODO
 * @Author 胡泽
 * @Date 2019/12/17 13:02
 * @Version 1.0
 */
@Component
public class ReceiveHandler {


    @Autowired
    private AliyunTextDetection aliyunTextDetection;

    @Autowired
    private AliyunAudioDetection aliyunAudioDetection;

    @Autowired
    private AliyunImageDetection aliyunImageDetection;


    //监听text队列
    @RabbitListener(queues = {QUEUE_INFORM_TEXT})
    public void receive_text(Object msg, Message message, Channel channel) throws Exception {
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        Map map = aliyunTextDetection.greenTextDetection(content);
        Map data = (Map) map.get("data");

//        回调函数  使用什么框架调用？  使用一个回调？
        if ((data.get("labels").equals(""))){
            System.out.println("通过审核!");
        }else {
            //        未通过审核参数
            System.out.println("未通过审核！");
            System.out.printf("reason : %s\n", JSON.toJSONString(data.get("reason")));
            System.out.printf("labels : %s\n", JSON.toJSONString(data.get("labels")));
        }

    }


    //监听image队列
    @RabbitListener(queues = {QUEUE_INFORM_IMAGE})
    public void receive_image(Object msg, Message message, Channel channel) throws Exception {
        String url = new String(message.getBody(), StandardCharsets.UTF_8);
        Map map =  aliyunImageDetection.greenImageDetection(url);
        Map data = (Map) map.get("data");
        System.out.println(data);
    }


    //监听audio队列
    @RabbitListener(queues = {QUEUE_INFORM_AUDIO})
    public void receive_audio(Object msg, Message message, Channel channel) throws Exception {
        String url = new String(message.getBody(), StandardCharsets.UTF_8);
        Map map = aliyunAudioDetection.greenAudioDetection(url);
        System.out.println(map);


    }

}