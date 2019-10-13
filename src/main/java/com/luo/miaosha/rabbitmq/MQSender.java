package com.luo.miaosha.rabbitmq;

import com.luo.miaosha.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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

    public void sendFanout(Object message) {
        String s = RedisService.beanToString(message);
        log.info("send fanout msg:{}", s);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", s);
    }

    public void sendHeaders(Object message) {
        String s = RedisService.beanToString(message);
        log.info("send headers msg:{}", s);
        MessageProperties prop = new MessageProperties();
        prop.setHeader("header1", "value1");
        prop.setHeader("header2", "value2");
        Message obj = new Message(s.getBytes(), prop);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String s = RedisService.beanToString(message);
        log.info("send miaosha msg:{}", s);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, s);

    }
}
