package com.rvc.designpattern.strategy.impl;

import com.aliyun.green20220302.models.VoiceModerationResponse;
import com.aliyun.green20220302.models.VoiceModerationResponseBody;
import com.rvc.designpattern.strategy.DetectionStrategy;
import com.rvc.mq.handler.ProducerHandler;
import com.rvc.pojo.DetectionStatusDto;
import com.rvc.pojo.DetectionTaskDto;
import com.rvc.sdk.aliyun.AliAudioDetection;
import com.rvc.sdk.aliyun.AliImageDetection;
import com.rvc.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.rvc.constant.DetectionConstant.NON_LABEL;

/**
 * @NAME: AudioDetectionStrategy
 * @USER: yuech
 * @Description:
 * @DATE: 2024/1/18
 */
@Component
public class AudioDetectionStrategy implements DetectionStrategy {

    @Autowired
    private AliAudioDetection aliAudioDetection;
    @Override
    public void process(DetectionTaskDto detectionTaskDto) throws Exception {

        VoiceModerationResponse response = (VoiceModerationResponse) aliAudioDetection.greenDetection(detectionTaskDto.getContent());
        VoiceModerationResponseBody result = response.getBody();
        VoiceModerationResponseBody.VoiceModerationResponseBodyData data = result.getData();

        String labels = aliAudioDetection.getRes(data.getTaskId());
        if (labels.isBlank()){
            labels = NON_LABEL;
        }
        DetectionStatusDto detectionStatusDto = DetectionStatusDto.builder()
                .id(detectionTaskDto.getId())
                .labels(labels)
                .build();
        ProducerHandler producerHandler = BeanUtils.getBean(ProducerHandler.class);
        producerHandler.submit(detectionStatusDto, detectionTaskDto.getRouterKey());
    }
}