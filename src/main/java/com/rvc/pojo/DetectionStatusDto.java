package com.rvc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @NAME: DetectionStatusDto
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetectionStatusDto {

    private Long id;
//展示状态(是否违规) 1：展示，0：不可展示
    private Integer status;

//    private String labels;

    private String violationInformation;

    private String name;
}