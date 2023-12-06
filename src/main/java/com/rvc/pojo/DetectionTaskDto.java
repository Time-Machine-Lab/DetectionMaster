package com.rvc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetectionTaskDto   {
//添加状态码

    private Long id;
//    内容
    private String content;
//    业务名
    private String name;

}
