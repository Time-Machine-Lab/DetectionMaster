package com.rvc.mq.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.rvc.constant.DetectionConstant.*;

@Slf4j
@Component
public class ProducerHandler {

    @Resource
    RabbitTemplate rabbitTemplate;
    public void submit(Object submit,String type) {
        rabbitTemplate.convertAndSend(RES_EXCHANGE_NAME, ROUTER_KEY_HEADER + type, JSON.toJSONString(submit));
    }

}