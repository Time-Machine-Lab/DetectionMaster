package com.rvc.designpattern.strategy.impl;

import com.aliyun.green20220302.models.TextModerationResponse;
import com.rvc.designpattern.strategy.DetectionStrategy;
import com.rvc.mq.handler.ProducerHandler;
import com.rvc.pojo.DetectionStatusDto;
import com.rvc.pojo.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliTextDetection;
import com.rvc.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rvc.constant.DetectionConstant.NON_LABEL;

/**
 * @NAME: TextDetectionStrategy
 * @USER: yuech
 * @Description:
 * @DATE: 2024/1/18
 */
@Component
public class TextDetectionStrategy implements DetectionStrategy {

    @Autowired
    private AliTextDetection aliTextDetection;
    @Override
    public void process(DetectionTaskDto detectionTaskDto) throws Exception {
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
            labels = NON_LABEL;
        }
        DetectionStatusDto detectionStatusDto = DetectionStatusDto.builder()
                .id(detectionTaskDto.getId())
                .labels(labels)
                .build();
        ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
        producerHandler.submit(detectionStatusDto,detectionTaskDto.getRouterKey());
    }


    public DetectionStatusDto getRes(DetectionTaskDto detectionTaskDto) throws Exception {
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
            labels = NON_LABEL;
        }
        DetectionStatusDto detectionStatusDto = DetectionStatusDto.builder()
                .id(detectionTaskDto.getId())
                .labels(labels)
                .build();
        return detectionStatusDto;
    }
}