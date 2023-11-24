package com.rvc;

//import com.rvc.utils.aliyun.GreenTextScan;
import com.alibaba.fastjson.JSONObject;
import com.rvc.sdk.aliyun.AliyunAudioDetection;
import com.rvc.sdk.aliyun.AliyunImageDetection;
import com.rvc.sdk.aliyun.AliyunTextDetection;
import com.rvc.sdk.tcyun.TcyunAudioDetection;
import com.rvc.sdk.tcyun.TcyunImageDetection;
import com.rvc.sdk.tcyun.TcyunTextDetection;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @NAME: AppTest
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/22
 */
@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class AppTest {

    /**
     * 阿里云
     */
    @Autowired
    private AliyunImageDetection aliyunImageDetection;

    @Autowired
    private AliyunTextDetection aliyunTextDetection;

    @Autowired
    private AliyunAudioDetection aliyunAudioDetection;



    /**
     * 腾讯云
     */
    @Autowired
    private TcyunTextDetection tcyunTextDetection;

    @Autowired
    private TcyunImageDetection tcyunImageDetection;

    @Autowired
    private TcyunAudioDetection tcyunAudioDetection;


    /**
     * 阿里云文本内容审核测试
     */
    @Test
    public void alTestScanText() throws Exception {
        String content = "你好，冰毒";
        Map map = aliyunTextDetection.greenTextDetection(content);
        System.out.println(map);
    }

    /**
     * 阿里云图片内容审核测试
     */
    @Test
    public void alTestScanImage() throws Exception {
        String imageUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.748160bf925a7acb3ba1c9514bbc60db?rik=AYY%2bJ9WcXYIMgw&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50017%2f0822.jpg_wh1200.jpg&ehk=CMVcdZMU6xxsjVjafO70cFcmJvD62suFC1ytk8UuAUk%3d&risl=&pid=ImgRaw&r=0";
        Map map = aliyunImageDetection.greenImageDetection(imageUrl);
        System.out.println(map);
    }
    /**
     * 阿里云音频内容审核测试
     */
    @Test
    public void alTestScanAudio() throws Exception {
        String audioUrl = "http://downsc.chinaz.net/Files/DownLoad/sound1/201906/11582.mp3";
        Map map = aliyunAudioDetection.greenAudioDetection(audioUrl);
        System.out.println(map);
    }



    /**
     * 腾讯云文本内容审核测试
     */
    @Test
    public void tcTestScanText() throws Exception {
        String content = "你好";
        Map map = tcyunTextDetection.greenTextDetection(content);
        System.out.println(map);
    }

    /**
     *  腾讯云图片内容审核测试
     */
    @Test
    public void tcTestScanImage() throws Exception {
        String imageUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.748160bf925a7acb3ba1c9514bbc60db?rik=AYY%2bJ9WcXYIMgw&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50017%2f0822.jpg_wh1200.jpg&ehk=CMVcdZMU6xxsjVjafO70cFcmJvD62suFC1ytk8UuAUk%3d&risl=&pid=ImgRaw&r=0";
        Map map = tcyunImageDetection.greenImageDetection(imageUrl);
        System.out.println(map);
    }
    /**
     *  腾讯云音频内容审核测试
     */
    @Test
    public void tcTestScanAudio() throws Exception {
        String audioUrl = "http://downsc.chinaz.net/Files/DownLoad/sound1/201906/11582.mp3";
        Map map = tcyunAudioDetection.greenAudioDetection(audioUrl);
        System.out.println(map);
    }


}