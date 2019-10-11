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

    public void sendTopic(Object message) {
        String s = RedisService.beanToString(message);
        log.info("send topic msg:{}", s);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", s);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", s);
    }
}
