package com.rvc.sdk.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @NAME: AliyunAudioDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/23
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")//报错是正常的
public class AliyunAudioDetection {
    private String secretId;
    private String secretKey;


    public JSONObject greenAudioDetection(String audioUrl) throws Exception {
        VoiceModerationResponse response = invokeFunction(audioUrl,secretId, secretKey, "green-cip.cn-shanghai.aliyuncs.com");

        // 自动路由。
        if (response != null) {
            //区域切换到cn-beijing。
            if (500 == response.getStatusCode() || (response.getBody() != null && 500 == (response.getBody().getCode()))) {
                // 接入区域和地址请根据实际情况修改。
                response = invokeFunction(audioUrl,secretId, secretKey, "green-cip.cn-beijing.aliyuncs.com");
            }
        }
        JSONObject  result =(JSONObject) JSON.toJSON(response.getBody());
        Map data = (Map) result.get("data");
        String taskId = (String) data.get("taskId");



//        结果获取的代码
        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        System.out.println(taskId);
        serviceParameters.put("taskId",taskId);
        Client client = createClient(secretId, secretKey, "green-cip.cn-shanghai.aliyuncs.com");
        com.aliyun.green20220302.models.VoiceModerationResultRequest voiceModerationResultRequest = new com.aliyun.green20220302.models.VoiceModerationResultRequest()
                .setService("audio_media_detection")
                .setServiceParameters(JSON.toJSONString(serviceParameters));;
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        VoiceModerationResultResponse voiceModerationResultResponse = client.voiceModerationResultWithOptions(voiceModerationResultRequest, runtime);
        VoiceModerationResultResponseBody body = voiceModerationResultResponse.getBody();
        while (body.code == 280){
             voiceModerationResultResponse = client.voiceModerationResultWithOptions(voiceModerationResultRequest, runtime);
             body = voiceModerationResultResponse.getBody();
        }
        System.out.println("-------------------------");
        System.out.println(JSON.toJSONString(body));
        return result;
        // 打印检测结果。
//            if (response != null) {
//                if (response.getStatusCode() == 200) {
//                    ImageModerationResponseBody body = response.getBody();
//                    System.out.println("requestId=" + body.getRequestId());
//                    System.out.println("code=" + body.getCode());
//                    System.out.println("msg=" + body.getMsg());
//                    if (body.getCode() == 200) {
//                        ImageModerationResponseBody.ImageModerationResponseBodyData data = body.getData();
//                        System.out.println("dataId=" + data.getDataId());
//                        List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> results = data.getResult();
//                        for (ImageModerationResponseBody.ImageModerationResponseBodyDataResult result : results) {
//                            System.out.println("label=" + result.getLabel());
//                            System.out.println("confidence=" + result.getConfidence());
//                        }
//                    } else {
//                        System.out.println("image moderation not success. code:" + body.getCode());
//                    }
//                } else {
//                    System.out.println("response not success. status:" + response.getStatusCode());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




    }



    public static Client createClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
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

    public static VoiceModerationResponse invokeFunction(String audioUrl,String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        //注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能。
        Client client = createClient(accessKeyId, accessKeySecret, endpoint);

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("url",audioUrl);
        //待检测数据唯一标识
        serviceParameters.put("dataId", UUID.randomUUID().toString());

        VoiceModerationRequest voiceModerationRequest = new VoiceModerationRequest();
        // 图片检测service：内容安全控制台图片增强版规则配置的serviceCode，示例：baselineCheck
        voiceModerationRequest.setService("audio_media_detection");
        voiceModerationRequest.setServiceParameters(JSON.toJSONString(serviceParameters));

        VoiceModerationResponse response = client.voiceModeration(voiceModerationRequest);


//        if (response.getStatusCode() == 200) {
//            VoiceModerationResponseBody result = response.getBody();
//            System.out.println(JSON.toJSONString(result));
//            System.out.println("requestId = " + result.getRequestId());
//            System.out.println("code = " + result.getCode());
//            System.out.println("msg = " + result.getMessage());
//            Integer code = result.getCode();
//            if (200 == code) {
//                VoiceModerationResponseBody.VoiceModerationResponseBodyData data = result.getData();
//                System.out.println("taskId = [" + data.getTaskId() + "]");
//            } else {
//                System.out.println("voice moderation not success. code:" + code);
//            }
//        } else {
//            System.out.println("response not success. status:" + response.getStatusCode());
//        }

        return response;
    }
}