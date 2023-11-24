package com.rvc.sdk.tcyun;
 
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 文本审核
 *  需要的参数：密钥信息、待检测文本（String类型）
 * 注意事项：不能直接将文本扔进去检测，检测之前需要对文本进行Base64加密
 * 文本内容大小支持：文本原文长度不能超过unicode编码长度10000个字符；
 * 文本审核语言支持：目前支持中文、英文、阿拉伯数字的检测；
 * 默认接口请求频率限制：1000次/秒，超过该频率限制则接口会报错
 *  默认接口请求频率限制：1000次/秒。
 * 返回类型：JSON
 */


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tcyun")//报错是正常的
public class TcyunTextDetection {
    private String secretId;
    private String secretKey;
    public JSONObject greenTextDetection(String text) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        Credential cred = new Credential(secretId, secretKey);
 
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tms.tencentcloudapi.com");
 
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
 
        // 实例化要请求产品的client对象,clientProfile是可选的
        TmsClient client = new TmsClient(cred, "ap-guangzhou", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        TextModerationRequest req = new TextModerationRequest();
 
        //Base64加密
        String encryptionText = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        //设置内容参数
        req.setContent(encryptionText);
 
        // 返回的resp是一个TextModerationResponse的实例，与请求对象对应
        TextModerationResponse resp = client.TextModeration(req);
 
        // 输出json格式的字符串回包
        String result = TextModerationResponse.toJsonString(resp);
        return JSON.parseObject(result);
    }
}