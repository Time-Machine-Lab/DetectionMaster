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


//    private String uuid;

    private String id;

//由调用者来判断是否违规
    private String labels;

}