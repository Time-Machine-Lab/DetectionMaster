package com.rvc.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetectionTaskDto   {
    private Long id;
//    内容
    private String content;
//    回调
    private String url;

}