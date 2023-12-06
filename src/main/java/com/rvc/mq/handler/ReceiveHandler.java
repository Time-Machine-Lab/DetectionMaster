package com.rvc.mq.handler;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.models.*;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.rvc.pojo.DetectionStatusDto;
import com.rvc.pojo.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliAudioDetection;
import com.rvc.sdk.aliyun.AliImageDetection;
import com.rvc.sdk.aliyun.AliTextDetection;

import com.rvc.utils.BeanUtils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    //监听text队列
//    @RabbitListener(queues = {QUEUE_INFORM_TEXT})
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "detection.text"),
            exchange = @Exchange(name = "detection.topic",type = ExchangeTypes.TOPIC),
            key = "detection.text"
    ))
    public void receive_text(Message message) throws Exception {
        System.out.println("detection.text");

        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        TextModerationResponse response = (TextModerationResponse) aliTextDetection.greenDetection(detectionTaskDto.getContent());

        JSONObject result =(JSONObject) JSON.toJSON(response.getBody());
        Map data = (Map) result.get("data");


        //        回调函数  使用什么框架调用？  使用一个回调？
        DetectionStatusDto detectionStatusDto = null;
        if (Objects.isNull(data)||data.get("labels").equals("")){
            detectionStatusDto = DetectionStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_DO_SHOW)
                    .name(detectionTaskDto.getName())
                    .violationInformation("nonLabel")
                    .build();

//            commentApi.status(commentStatusDto);
        }else {
            detectionStatusDto = DetectionStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_NOT_SHOW)
                    .name(detectionTaskDto.getName())
                    .violationInformation(JSON.toJSONString(data.get("reason")))
                    .build();

        }

        ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
        producerHandler.submit(detectionStatusDto,"text");


    }


    //监听image队列
//    @RabbitListener(queues = {QUEUE_INFORM_IMAGE})
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "detection.image"),
            exchange = @Exchange(name = "detection.topic",type = ExchangeTypes.TOPIC),
            key = "detection.image"
    ))
    public void receive_image(Message message) throws Exception {

        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);
        if (Strings.isBlank(detectionTaskDto.getContent())){
            return;
        }

        ImageModerationResponse response = (ImageModerationResponse) aliImageDetection.greenDetection(detectionTaskDto.getContent());

        DetectionStatusDto detectionStatusDto = null;
        ImageModerationResponseBody body = response.getBody();
        ImageModerationResponseBody.ImageModerationResponseBodyData data = body.getData();
        List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> results = data.getResult();
        for (ImageModerationResponseBody.ImageModerationResponseBodyDataResult result : results) {
            if ( result.getLabel().equals("nonLabel")){
                detectionStatusDto = DetectionStatusDto.builder()
                        .id(detectionTaskDto.getId())
                        .status(STATUS_DO_SHOW)
                        .name(detectionTaskDto.getName())
                        .violationInformation("nonLabel")
                        .build();
            }else {
                detectionStatusDto = DetectionStatusDto.builder()
                        .id(detectionTaskDto.getId())
                        .status(STATUS_NOT_SHOW)
                        .name(detectionTaskDto.getName())
                        .violationInformation(result.getLabel())
                        .build();
                break;
            }
        }

        ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
        producerHandler.submit(detectionStatusDto,"image");
    }


    //监听audio队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "detection.audio"),
            exchange = @Exchange(name = "detection.topic",type = ExchangeTypes.TOPIC),
            key = "detection.audio"
    ))
    @RabbitListener(queues = {QUEUE_INFORM_AUDIO})
    public void receive_audio( Message message) throws Exception {
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

        VoiceModerationResponse response = (VoiceModerationResponse) aliAudioDetection.greenDetection(detectionTaskDto.getContent());
        VoiceModerationResponseBody result = response.getBody();
        VoiceModerationResponseBody.VoiceModerationResponseBodyData data = result.getData();
//                System.out.println("taskId = [" + data.getTaskId() + "]");
        /**
         * 获取结果
         */
        String res = aliAudioDetection.getRes(data.getTaskId());
        DetectionStatusDto detectionStatusDto = null;
        if (res == "nonLabel") {
            detectionStatusDto = DetectionStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_DO_SHOW)
                    .name(detectionTaskDto.getName())
                    .violationInformation(res)
                    .build();
        } else {
            detectionStatusDto = DetectionStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_NOT_SHOW)
                    .name(detectionTaskDto.getName())
                    .violationInformation(res)
                    .build();

        }
        ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
        producerHandler.submit(detectionStatusDto, "audio");
    }





}