package com.rvc.mq.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.rvc.constant.DetectionConstant.EXCHANGE_TOPICS_INFORM;
import static com.rvc.constant.DetectionConstant.EXCHANGE_TOPICS_STATUS;

@Slf4j
@Component
public class ProducerHandler {

    @Resource
    RabbitTemplate rabbitTemplate;
//
    public void submit(Object submit,String type) {
        String exchangeName ="res.topic";
        rabbitTemplate.convertAndSend(exchangeName, "res." + type, JSON.toJSONString(submit));
    }

}