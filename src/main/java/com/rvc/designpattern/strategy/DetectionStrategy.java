package com.rvc.designpattern.strategy;

import com.rvc.pojo.DetectionStatusDto;
import com.rvc.pojo.DetectionTaskDto;

public interface DetectionStrategy {
    void process(DetectionTaskDto detectionTaskDto) throws Exception;

    DetectionStatusDto getRes(DetectionTaskDto detectionTaskDto) throws Exception;
}
