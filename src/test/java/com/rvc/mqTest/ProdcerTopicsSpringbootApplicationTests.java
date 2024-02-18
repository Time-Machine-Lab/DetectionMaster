package com.rvc.mqTest;

import com.alibaba.fastjson.JSON;
import com.rvc.pojo.DetectionTaskDto;
import com.rvc.pojo.DetectionTaskListDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;


@SpringBootTest
@RunWith(SpringRunner.class)
class ProdcerTopicsSpringbootApplicationTests {
    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    public void Producer_topics_springbootTest() {

        //使用rabbitTemplate发送消息
        String message = "你好,冰毒";
        String audioUrl = "http://downsc.chinaz.net/Files/DownLoad/sound1/201906/11582.mp3";
        String imageUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.748160bf925a7acb3ba1c9514bbc60db?rik=AYY%2bJ9WcXYIMgw&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50017%2f0822.jpg_wh1200.jpg&ehk=CMVcdZMU6xxsjVjafO70cFcmJvD62suFC1ytk8UuAUk%3d&risl=&pid=ImgRaw&r=0";


        /**
         * 参数：
         * 1、交换机名称
         * 2、routingKey
         * 3、消息内容
         */
        DetectionTaskDto detectionTaskDto1 = new DetectionTaskDto();
        DetectionTaskDto detectionTaskDto2 = new DetectionTaskDto();
        DetectionTaskDto detectionTaskDto3 = new DetectionTaskDto();
        DetectionTaskListDto detectionTaskListDto = new DetectionTaskListDto(new ArrayList<DetectionTaskDto>());
        detectionTaskListDto.getTaskList().add(detectionTaskDto1);
        detectionTaskListDto.getTaskList().add(detectionTaskDto2);
        detectionTaskListDto.getTaskList().add(detectionTaskDto3);
        rabbitTemplate.convertAndSend("detection.topic","detection.topic.list.key", JSON.toJSONString(detectionTaskListDto));

    }




}