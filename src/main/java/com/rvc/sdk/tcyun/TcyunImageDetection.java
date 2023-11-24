package com.rvc.sdk.tcyun;
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ims.v20201229.ImsClient;
import com.tencentcloudapi.ims.v20201229.models.*;

/**
 * 图片内容安全
 *  接口调用说明：
 * 需要的参数：密钥信息、待检测图片URL路径
 * 图片文件大小支持：文件 < 5M
 * 图片文件分辨率支持：建议分辨率大于256x256，否则可能会影响识别效果；
 * 图片文件支持格式：PNG、JPG、JPEG、BMP、GIF、WEBP格式；
 * 图片文件链接支持的传输协议：HTTP、HTTPS；
 * 若传入图片文件的访问链接，则需要注意图片下载时间限制为3秒，为保障被检测图片的稳定性和可靠性，建议您使用腾讯云COS存储或者CDN缓存等；
 * 默认接口请求频率限制：100次/秒，超过此调用频率则会报错。
 * 返回类型：JSON
 */


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tcyun")//报错是正常的
public class TcyunImageDetection {
    private String secretId;
    private String secretKey;
 
    public JSONObject greenImageDetection(String imageUrl) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        Credential cred = new Credential(secretId, secretKey);
 
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ims.tencentcloudapi.com");
 
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
 
        // 实例化要请求产品的client对象,clientProfile是可选的
        ImsClient client = new ImsClient(cred, "ap-guangzhou", clientProfile);
 
        // 实例化一个请求对象,每个接口都会对应一个request对象
        ImageModerationRequest req = new ImageModerationRequest();
        //设置图片url地址
        req.setFileUrl(imageUrl);
 
        // 返回的resp是一个ImageModerationResponse的实例，与请求对象对应
        ImageModerationResponse resp = client.ImageModeration(req);
 
        // 输出json格式的字符串回包
        String result = ImageModerationResponse.toJsonString(resp);
 
        return JSON.parseObject(result);
    }
}