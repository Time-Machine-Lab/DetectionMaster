package com.rvc.sdk.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
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
 * @NAME: AliyunTextDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/23
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")//报错是正常的
public class AliyunTextDetection{
    private String secretId;
    private String secretKey;

    public JSONObject greenTextDetection(String content) throws Exception {
        TextModerationResponse response = invokeFunction(content,secretId, secretKey, "green-cip.cn-shanghai.aliyuncs.com");

        // 自动路由。
        if (response != null) {
            //区域切换到cn-beijing。
            if (500 == response.getStatusCode() || (response.getBody() != null && 500 == (response.getBody().getCode()))) {
                // 接入区域和地址请根据实际情况修改。

                response = invokeFunction(content,secretId, secretKey, "green-cip.cn-beijing.aliyuncs.com");
            }
        }

        // 打印检测结果。
//        if (response != null) {
//            if (response.getStatusCode() == 200) {
//                TextModerationResponseBody result = response.getBody();
//                System.out.println(JSON.toJSONString(result));
//                Integer code = result.getCode();
//                if (code != null && code == 200) {
//                    TextModerationResponseBody.TextModerationResponseBodyData data = result.getData();
//                    System.out.println("labels = [" + data.getLabels() + "]");
//                    System.out.println("reason = [" + data.getReason() + "]");
//                } else {
//                    System.out.println("text moderation not success. code:" + code);
//                }
//            } else {
//                System.out.println("response not success. status:" + response.getStatusCode());
//            }
//        }
        JSONObject  result =(JSONObject) JSON.toJSON(response.getBody());

        return result;

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

    public static TextModerationResponse invokeFunction(String content,String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        //注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能。
        Client client = createClient(accessKeyId, accessKeySecret, endpoint);

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("content",content);
        //待检测数据唯一标识
        serviceParameters.put("dataId", UUID.randomUUID().toString());


        TextModerationRequest request = new TextModerationRequest();
        // 图片检测service：内容安全控制台图片增强版规则配置的serviceCode，示例：baselineCheck
        request.setService("comment_detection");
        request.setServiceParameters(JSON.toJSONString(serviceParameters));

        TextModerationResponse response = client.textModerationWithOptions(request, runtime);

//        try {
//            response = client.imageModerationWithOptions(request, runtime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return response;
    }
}