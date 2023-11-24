package com.rvc.sdk.aliyun;


import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.VoiceModerationResultRequest;
import com.aliyun.green20220302.models.VoiceModerationResultResponse;
import com.aliyun.green20220302.models.VoiceModerationResultResponseBody;

import com.aliyun.teaopenapi.models.Config;
import net.minidev.json.JSONObject;

/**
 * @NAME: AliyunAudioModerationResult
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/23
 */
public class AliyunAudioModerationResult {
    public static void main(String[] args) throws Exception {
        Config config = new Config();
        /**
         * 阿里云账号AccessKey拥有所有API的访问权限，建议您使用RAM用户进行API访问或日常运维。
         * 常见获取环境变量方式：
         * 方式一：
         *     获取RAM用户AccessKeyID：System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
         *     获取RAM用户AccessKeySecret：System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
         * 方式二：
         *     获取RAM用户AccessKey ID：System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_ID");
         *     获取RAM用户AccessKey Secret：System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
         */
        config.setAccessKeyId("LTAI5tSBJDjWYyHtfE2HEGyN");
        config.setAccessKeySecret("jtIyL0fAF6ZgVIxUDXPtcJjvT2YHYr");
        // 接入区域和地址请根据实际情况修改。
        config.setRegionId("cn-shanghai");
        config.setEndpoint("green-cip.cn-shanghai.aliyuncs.com");
        // 连接时超时时间，单位毫秒（ms）。
        config.setReadTimeout(6000);
        // 读取时超时时间，单位毫秒（ms）。
        config.setConnectTimeout(3000);

        // 注意：此处实例化的client尽可能重复使用，提升检测性能。避免重复建立连接。
        Client client = new Client(config);

        JSONObject serviceParameters = new JSONObject();
        // 提交任务时返回的taskId。
        serviceParameters.put("taskId", "au_f_IzPzTdv6r9cfgw80SeTwx4-1y$MGB");


        VoiceModerationResultRequest voiceModerationResultRequest = new VoiceModerationResultRequest();
        // 检测类型：audio_media_detection表示语音文件检测，live_stream_detection表示语音直播流检测。
        voiceModerationResultRequest.setService("audio_media_detection");
        voiceModerationResultRequest.setServiceParameters(serviceParameters.toJSONString());

        try {
            VoiceModerationResultResponse response = client.voiceModerationResult(voiceModerationResultRequest);
            if (response.getStatusCode() == 200) {
                VoiceModerationResultResponseBody result = response.getBody();
                System.out.println("requestId=" + result.getRequestId());
                System.out.println("code=" + result.getCode());
                System.out.println("msg=" + result.getMessage());
                if (200 == result.getCode()) {
                    VoiceModerationResultResponseBody.VoiceModerationResultResponseBodyData data = result.getData();
                    System.out.println("sliceDetails = " + JSON.toJSONString(data.getSliceDetails()));
                    System.out.println("taskId = " + data.getTaskId());
                    System.out.println("url = " + data.getUrl());
                } else {
                    System.out.println("voice moderation result not success. code:" + result.getCode());
                }
            } else {
                System.out.println("response not success. status:" + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}