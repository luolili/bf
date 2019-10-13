package com.luo.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String msg) {
        log.info("get msg:{}", msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String msg) {
        log.info("get topic1 msg:{}", msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String msg) {
        log.info("get topic1 msg:{}", msg);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeaders(byte[] msg) {
        log.info("get header msg:{}", new String(msg));
    }

}
