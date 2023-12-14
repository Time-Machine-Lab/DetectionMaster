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

           String labels  = "";
           int len = detectionTaskDto.getContent().length();
           //如果内容过长   分段进行审核
           while (len > 0){
               //获取审核结果
               int start = len-400 >0?len-400:0;
               String text = detectionTaskDto.getContent().substring(start,len);
               TextModerationResponse response = (TextModerationResponse) aliTextDetection.greenDetection(text);
               labels += aliTextDetection.getLabels(response);
               len -=400;
            }

           if (labels.isBlank()){
               labels = "nonLabel";
           }
        DetectionStatusDto  detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .labels(labels)
                       .name(detectionTaskDto.getName())
                       .build();

           ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
           producerHandler.submit(detectionStatusDto,"text");
       }
       catch (Exception e) {
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


            ImageModerationResponse response = (ImageModerationResponse) aliImageDetection.greenDetection(detectionTaskDto.getContent());
            String labels = aliImageDetection.getLabels(response);


            if (labels.isBlank()){
                labels = "nonLabel";
            }
            DetectionStatusDto detectionStatusDto = DetectionStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .name(detectionTaskDto.getName())
                    .labels(labels)
                    .build();

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
           String labels = aliAudioDetection.getRes(data.getTaskId());

           if (labels.isBlank()){
               labels = "nonLabel";
           }
           DetectionStatusDto detectionStatusDto = DetectionStatusDto.builder()
                       .id(detectionTaskDto.getId())
                       .labels(labels)
                       .name(detectionTaskDto.getName())
                       .build();
           ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
           producerHandler.submit(detectionStatusDto, "audio");
       }catch (Exception e){
           log.info("audio err");
       }
    }





}