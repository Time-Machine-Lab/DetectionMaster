package com.rvc.sdk.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.rvc.sdk.AbstractAliDetection;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.rvc.constant.DetectionConstant.COMMENT_DETECTION;

/**
 * @NAME: AliTextDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/12/1
 */

@Component
public class AliTextDetection extends AbstractAliDetection {
    @Override
    public TextModerationResponse invokeFunction(String content, String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
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
        request.setService(COMMENT_DETECTION);
        request.setServiceParameters(JSON.toJSONString(serviceParameters));

        TextModerationResponse response = client.textModerationWithOptions(request, runtime);

        return response;
    }
}