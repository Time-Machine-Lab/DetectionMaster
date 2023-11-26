package com.rvc.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @NAME: RejectedBo
 * @USER: yuech
 * @Description:审核不通过的参数
 * @DATE: 2023/11/26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectedBo {

//        风险提示
    private String riskTips;
//        违禁词汇
    private String riskWords;
}