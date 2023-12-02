package com.rvc.service;


import com.rvc.pojo.DetectionResult;

public interface DetectionService {
    public DetectionResult calculateTextDetection();

    public DetectionResult calculateImageDetection();

    public DetectionResult calculateAudioDetection();
}
