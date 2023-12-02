package com.rvc.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @NAME: CommentStatusDto
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/30
 */
@Data
@Builder
public class CommentStatusDto {

    private Long id;
//展示状态(是否违规) 1：展示，0：不可展示
    private Integer status;

    private String violationInformation;
}