package com.luo.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    //direct模式
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String msg) {
        log.info("get msg:{}", msg);
    }
}
