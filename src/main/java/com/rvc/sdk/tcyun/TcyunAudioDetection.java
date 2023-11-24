package com.rvc.sdk.tcyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.ams.v20200608.AmsClient;
import com.tencentcloudapi.ams.v20200608.models.CreateAudioModerationTaskRequest;
import com.tencentcloudapi.ams.v20200608.models.CreateAudioModerationTaskResponse;
import com.tencentcloudapi.ams.v20200608.models.StorageInfo;
import com.tencentcloudapi.ams.v20200608.models.TaskInput;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @NAME: TcyunAudioDetection
 * @USER: yuech
 * @Description: 对音频进行审核
 * @DATE: 2023/11/22
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tcyun")//报错是正常的
public class TcyunAudioDetection {
    private String secretId;
    private String secretKey;

    public JSONObject greenAudioDetection(String url) throws TencentCloudSDKException {
// 实例化一个认证对象，传入腾讯云账户的 secretId 和 secretKey
        Credential cred = new Credential(secretId, secretKey);

// 实例化一个 http 选项，设置请求的终端节点
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ams.tencentcloudapi.com");

// 实例化一个 client 选项，设置 http 选项
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

// 实例化要请求产品的 client 对象，传入认证对象、地域参数和 client 选项
        AmsClient client = new AmsClient(cred, "ap-guangzhou", clientProfile);

// 实例化一个请求对象，每个接口都会对应一个 request 对象
        CreateAudioModerationTaskRequest req = new CreateAudioModerationTaskRequest();

        TaskInput[] taskInputs = new TaskInput[1];
        TaskInput taskInput1 = new TaskInput();
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.setUrl(url);
        taskInput1.setInput(storageInfo);

        taskInputs[0] = taskInput1;

        req.setTasks(taskInputs);
//        req.setTasks(taskInputs);

// 返回的resp是一个CreateAudioModerationTaskResponse的实例，与请求对象对应
        CreateAudioModerationTaskResponse resp = client.CreateAudioModerationTask(req);

// 将响应对象转换成 JSON 格式的字符串
        String result = CreateAudioModerationTaskResponse.toJsonString(resp);

// 使用 JSON 解析库将 JSON 字符串转换为对象，并返回结果
        return JSON.parseObject(result);

    }
}