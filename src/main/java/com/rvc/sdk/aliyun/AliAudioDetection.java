package com.rvc.sdk.aliyun;

import com.aliyun.green20220302.models.TextModerationResponse;
import com.rvc.sdk.AbstractAliDetection;
import org.springframework.stereotype.Component;

/**
 * @NAME: AliAudioDetection
 * @USER: yuech
 * @Description:
 * @DATE: 2023/12/2
 */
@Component
public class AliAudioDetection extends AbstractAliDetection {
    @Override
    public TextModerationResponse invokeFunction(String content, String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        return null;
    }
}