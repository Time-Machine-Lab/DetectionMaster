package com.rvc.mq.handler;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.models.ImageModerationResponse;
import com.aliyun.green20220302.models.TextModerationResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rvc.feign.CommentApi;

import com.rvc.pojo.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliAudioDetection;
import com.rvc.sdk.aliyun.AliImageDetection;
import com.rvc.sdk.aliyun.AliTextDetection;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.rvc.constant.DetectionConstant.*;


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
    private AliTextDetection aliTextDetection;

    @Autowired
    private AliAudioDetection aliAudioDetection;

    @Autowired
    private AliImageDetection aliImageDetection;

    @Autowired
    private CommentApi commentApi;


    //监听text队列
    @RabbitListener(queues = {QUEUE_INFORM_TEXT})
    public void receive_text(Message message) throws Exception {

        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        TextModerationResponse response = (TextModerationResponse) aliTextDetection.greenDetection(detectionTaskDto.getContent());

        JSONObject result =(JSONObject) JSON.toJSON(response.getBody());
        Map data = (Map) result.get("data");
        aliTextDetection.back(data,detectionTaskDto,commentApi);
    }


    //监听image队列
    @RabbitListener(queues = {QUEUE_INFORM_IMAGE})
    public void receive_image(Message message) throws Exception {

        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        ImageModerationResponse response = (ImageModerationResponse) aliImageDetection.greenDetection(detectionTaskDto.getContent());

        JSONObject result =(JSONObject) JSON.toJSON(response.getBody());
        Map data = (Map) result.get("data");
//        业务不同传的api不同
        aliTextDetection.back(data,detectionTaskDto,commentApi);

//        String content = new String(message.getBody(), StandardCharsets.UTF_8);
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);
//
////        Map map =  aliImageDetection.greenDetection(detectionTaskDto.getContent());
////        Map data = (Map) map.get("data");
////        aliImageDetection.back(data,detectionTaskDto,commentApi);
    }


    //监听audio队列
    @RabbitListener(queues = {QUEUE_INFORM_AUDIO})
    public void receive_audio( Message message) throws Exception {
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        TextModerationResponse response = (TextModerationResponse) aliAudioDetection.greenDetection(detectionTaskDto.getContent());

        JSONObject result =(JSONObject) JSON.toJSON(response.getBody());
        Map data = (Map) result.get("data");
//        业务不同传的api不同
        aliTextDetection.back(data,detectionTaskDto,commentApi);

    }

}