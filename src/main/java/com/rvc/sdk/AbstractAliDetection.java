package com.rvc.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.VoiceModerationResultRequest;
import com.aliyun.green20220302.models.VoiceModerationResultResponse;
import com.aliyun.green20220302.models.VoiceModerationResultResponseBody;
import com.aliyun.tea.TeaModel;
import com.aliyun.teaopenapi.models.Config;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
    public String getRes(String taskId) throws Exception {
        Client client = createClient(secretId, secretKey, "green-cip.cn-shanghai.aliyuncs.com");
        net.minidev.json.JSONObject serviceParameters = new JSONObject();
        // 提交任务时返回的taskId。
        System.out.println(taskId);
        serviceParameters.put("taskId", taskId);

        VoiceModerationResultRequest voiceModerationResultRequest = new VoiceModerationResultRequest();
        // 检测类型：audio_media_detection表示语音文件检测，live_stream_detection表示语音直播流检测。
        voiceModerationResultRequest.setService("AUDIO_MEDIA_DETECTION");
        voiceModerationResultRequest.setServiceParameters(serviceParameters.toJSONString());


        VoiceModerationResultResponse response = client.voiceModerationResult(voiceModerationResultRequest);
        VoiceModerationResultResponseBody result = response.getBody();
        while (result.getMessage() == "PROCESSING") {
            response = client.voiceModerationResult(voiceModerationResultRequest);
            result = response.getBody();
        }
        if (200 == result.getCode()) {
            VoiceModerationResultResponseBody.VoiceModerationResultResponseBodyData data = result.getData();
            List<VoiceModerationResultResponseBody.VoiceModerationResultResponseBodyDataSliceDetails> sliceDetails = data.getSliceDetails();
            for (VoiceModerationResultResponseBody.VoiceModerationResultResponseBodyDataSliceDetails sliceDetail : sliceDetails) {
                String msg = sliceDetail.getLabels();
                if (!Strings.isBlank(msg)) {
                    System.out.println(sliceDetail.getLabels());
                    return sliceDetail.getLabels();
                }
            }

        }

        return "nonLabel";
    }

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

        config.setEndpoint(endpoint);
        return new Client(config);
    }


    public  abstract TeaModel invokeFunction(String content, String accessKeyId, String accessKeySecret, String endpoint) throws Exception;

}