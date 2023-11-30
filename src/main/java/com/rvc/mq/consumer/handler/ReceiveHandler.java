package com.rvc.mq.consumer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rvc.feign.CommentApi;
import com.rvc.pojo.dto.CommentStatusDto;
import com.rvc.pojo.dto.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliyunAudioDetection;
import com.rvc.sdk.aliyun.AliyunImageDetection;
import com.rvc.sdk.aliyun.AliyunTextDetection;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.rvc.constant.DetectionConstants.*;

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


    @Autowired
    private CommentApi commentApi;


    //监听text队列
    @RabbitListener(queues = {QUEUE_INFORM_TEXT})
    public void receive_text(Object msg, Message message, Channel channel) throws Exception {

        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto textDetectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        Map map = aliyunTextDetection.greenTextDetection(textDetectionTaskDto.getContent());
        Map data = (Map) map.get("data");

//        回调函数
        if ((data.get("labels").equals(""))){
            CommentStatusDto commentStatusDto = CommentStatusDto.builder()
                    .id(textDetectionTaskDto.getId())
                    .status(STATUS_DO_SHOW)
                    .build();
            commentApi.status(commentStatusDto);
        }else {
            CommentStatusDto commentStatusDto = CommentStatusDto.builder()
                    .id(textDetectionTaskDto.getId())
                    .status(STATUS_NOT_SHOW)
                    .violationInformation(JSON.toJSONString(data.get("reason")))
                    .build();
            commentApi.status(commentStatusDto);

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