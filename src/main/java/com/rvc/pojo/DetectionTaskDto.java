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

    //审核 路由 key
    private String routerKey;

    private String type;

    private String id;
    //    内容
    private String content;
}
