package com.rvc.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.tea.TeaModel;
import com.aliyun.teaopenapi.models.Config;
import com.rvc.feign.CommentApi;
import com.rvc.pojo.CommentStatusDto;
import com.rvc.pojo.DetectionTaskDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.rvc.constant.DetectionConstant.STATUS_DO_SHOW;
import static com.rvc.constant.DetectionConstant.STATUS_NOT_SHOW;

/**
 * @NAME: AbstractAliDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/12/1
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")//报错是正常的
public abstract class AbstractAliDetection {
    private String secretId;
    private String secretKey;



    public TeaModel greenDetection(String content) throws Exception {
        TeaModel response = invokeFunction(content,secretId, secretKey, "green-cip.cn-shanghai.aliyuncs.com");

        // 自动路由。
//        if (response != null) {
//            //区域切换到cn-beijing。
//            if (500 == response.getStatusCode() || (response.getBody() != null && 500 == (response.getBody().getCode()))) {
//                // 接入区域和地址请根据实际情况修改。
//
//                response = invokeFunction(content,secretId, secretKey, "green-cip.cn-beijing.aliyuncs.com");
//            }
//        }
//        JSONObject  result =(JSONObject) JSON.toJSON(response.getBody());

        return response;
    }

    public static  Client createClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        // 设置http代理。
        //config.setHttpProxy("http://10.10.xx.xx:xxxx");
        // 设置https代理。
        //config.setHttpsProxy("https://10.10.xx.xx:xxxx");
        // 接入区域和地址请根据实际情况修改
        config.setEndpoint(endpoint);
        return new Client(config);
    }


    public  abstract TeaModel invokeFunction(String content, String accessKeyId, String accessKeySecret, String endpoint) throws Exception;


    public static void back(Map data, DetectionTaskDto detectionTaskDto,CommentApi commentApi){
        //        回调函数  使用什么框架调用？  使用一个回调？
        if ((data.get("labels").equals(""))){
            CommentStatusDto commentStatusDto = CommentStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_DO_SHOW)
                    .build();
            commentApi.status(commentStatusDto);
        }else {
            CommentStatusDto commentStatusDto = CommentStatusDto.builder()
                    .id(detectionTaskDto.getId())
                    .status(STATUS_NOT_SHOW)
                    .violationInformation(JSON.toJSONString(data.get("reason")))
                    .build();
            commentApi.status(commentStatusDto);
        }
    }
}