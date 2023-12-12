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

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReceiveHandler {


    @Autowired
    private AliTextDetection aliTextDetection;

    @Autowired
    private AliAudioDetection aliAudioDetection;

    @Autowired
    private AliImageDetection aliImageDetection;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = TEXT_QUEUE_NAME),
            exchange = @Exchange(name = DETECTION_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = TEXT_ROUTER_KEY
    ))
    public void receive_text(Message message) throws Exception {
       try{
           //转换消息格式
           String content = new String(message.getBody(), StandardCharsets.UTF_8);
           ObjectMapper objectMapper = new ObjectMapper();
           DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

           //获取审核结果
           TextModerationResponse response = (TextModerationResponse) aliTextDetection.greenDetection(detectionTaskDto.getContent());

           //判断结果
           JSONObject result =(JSONObject) JSON.toJSON(response.getBody());
           Map data = (Map) result.get("data");
           DetectionStatusDto detectionStatusDto = null;
           if (Objects.isNull(data)||data.get("labels").equals("")){
               detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .status(DETECTION_SUCCESS)
                       .name(detectionTaskDto.getName())
                       .violationInformation(NONLABEL)
//                    .labels((String) data.get("labels"))
                       .build();

           }else {
               detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .status(DETECTION_FAIL)
                       .name(detectionTaskDto.getName())
                       .violationInformation(JSON.toJSONString(data.get("reason")))
//                    .labels((String) data.get("labels"))
                       .build();

           }

           ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
           producerHandler.submit(detectionStatusDto,"text");
       }
       catch (Exception e){
           log.info("txt err");
       }
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = IMAGE_QUEUE_NAME),
            exchange = @Exchange(name = DETECTION_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = IMAGE_ROUTER_KEY
    ))
    public void receive_image(Message message) {
        try{

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
                if ( result.getLabel().equals(NONLABEL)){
                    detectionStatusDto = DetectionStatusDto.builder()
                            .id(detectionTaskDto.getId())
                            .status(DETECTION_SUCCESS)
                            .name(detectionTaskDto.getName())
                            .violationInformation(NONLABEL)
                            .build();
                }else {
                    detectionStatusDto = DetectionStatusDto.builder()
                            .id(detectionTaskDto.getId())
                            .status(DETECTION_FAIL)
                            .name(detectionTaskDto.getName())
                            .violationInformation(result.getLabel())
                            .build();
                    break;
                }
            }

            ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
            producerHandler.submit(detectionStatusDto,"image");
        }catch (Exception e){
            log.info("img err");
        }
    }



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AUDIO_QUEUE_NAME),
            exchange = @Exchange(name = DETECTION_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = AUDIO_ROUTER_KEY
    ))
    public void receive_audio( Message message) throws Exception {
       try{
           String content = new String(message.getBody(), StandardCharsets.UTF_8);
           ObjectMapper objectMapper = new ObjectMapper();

           DetectionTaskDto detectionTaskDto = objectMapper.readValue(content, DetectionTaskDto.class);

           VoiceModerationResponse response = (VoiceModerationResponse) aliAudioDetection.greenDetection(detectionTaskDto.getContent());
           VoiceModerationResponseBody result = response.getBody();
           VoiceModerationResponseBody.VoiceModerationResponseBodyData data = result.getData();

           /**
            * 获取结果
            */
           String res = aliAudioDetection.getRes(data.getTaskId());
           DetectionStatusDto detectionStatusDto = null;
           if (res == "nonLabel") {
               detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .status(DETECTION_SUCCESS)
                       .name(detectionTaskDto.getName())
                       .violationInformation(res)
                       .build();
           } else {
               detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .status(DETECTION_FAIL)
                       .name(detectionTaskDto.getName())
                       .violationInformation(res)
                       .build();

           }
           ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
           producerHandler.submit(detectionStatusDto, "audio");
       }catch (Exception e){
           log.info("audio err");
       }
    }





}