package com.rvc.service;

import com.rvc.pojo.ResponseResult;

public interface DetectionService {
    public ResponseResult calculateTextDetection();

    public ResponseResult calculateImageDetection();

    public ResponseResult calculateAudioDetection();
}
