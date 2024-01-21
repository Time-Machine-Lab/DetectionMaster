package com.rvc.designpattern.strategy.impl;

import com.aliyun.green20220302.models.ImageModerationResponse;
import com.rvc.designpattern.strategy.DetectionStrategy;
import com.rvc.mq.handler.ProducerHandler;
import com.rvc.pojo.DetectionStatusDto;
import com.rvc.pojo.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliImageDetection;
import com.rvc.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rvc.constant.DetectionConstant.NON_LABEL;

/**
 * @NAME: ImgDetectionStrategy
 * @USER: yuech
 * @Description:
 * @DATE: 2024/1/18
 */
@Component
public class ImgDetectionStrategy implements DetectionStrategy {

    @Autowired
    private AliImageDetection aliImageDetection;
    @Override
    public void process(DetectionTaskDto detectionTaskDto) throws Exception {
        ImageModerationResponse response = (ImageModerationResponse) aliImageDetection.greenDetection(detectionTaskDto.getContent());
        String labels = aliImageDetection.getLabels(response);
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
}