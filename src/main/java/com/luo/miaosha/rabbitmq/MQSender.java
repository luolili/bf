package com.luo.miaosha.rabbitmq;

import com.luo.miaosha.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String s = RedisService.beanToString(message);
        log.info("send msg:{}", s);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, s);
    }
}
