package com.rvc.sdk.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.rvc.sdk.AbstractAliDetection;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.rvc.constant.DetectionConstant.AUDIO_MEDIA_DETECTION;

/**
 * @NAME: AliAudioDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/12/2
 */
@Component
public class AliAudioDetection extends AbstractAliDetection {
    @Override
    public VoiceModerationResponse invokeFunction(String content, String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        //注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能。
        Client client = createClient(accessKeyId, accessKeySecret, endpoint);

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("url",content);
        //待检测数据唯一标识
        serviceParameters.put("dataId", UUID.randomUUID().toString());

        VoiceModerationRequest voiceModerationRequest = new VoiceModerationRequest();
        // 图片检测service：内容安全控制台图片增强版规则配置的serviceCode，示例：baselineCheck
        voiceModerationRequest.setService(AUDIO_MEDIA_DETECTION);
        voiceModerationRequest.setServiceParameters(JSON.toJSONString(serviceParameters));

        VoiceModerationResponse response = client.voiceModeration(voiceModerationRequest);

        return response;
    }
}