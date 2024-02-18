package com.rvc.pojo;

import lombok.Data;

import java.util.List;

/**
 * @NAME: DetectionTaskListDto
 * @USER: yuech
 * @Description:
 * @DATE: 2024/2/13
 */
@Data
public class DetectionTaskListDto {


    boolean sync = false;

    List<DetectionTaskDto> taskList;

    public DetectionTaskListDto() {
    }

    public DetectionTaskListDto(List<DetectionTaskDto> taskList) {
        this.taskList = taskList;
    }
}