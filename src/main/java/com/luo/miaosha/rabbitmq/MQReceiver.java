package com.luo.miaosha.rabbitmq;

import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.service.GoodsService;
import com.luo.miaosha.service.MiaoshaOrderService;
import com.luo.miaosha.service.RedisService;
import com.luo.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private MiaoshaOrderService miaoshaOrderService;
    @Autowired
    private GoodsService goodsService;
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

    //--
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaosha(String msg) {
        log.info("get msg:{}", msg);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(msg, MiaoshaMessage.class);

        MiaoshaUser user = miaoshaMessage.getUser();
        Integer goodsId = miaoshaMessage.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        Integer stock = goodsVo.getStockCount();
        if (stock < 0) {
            return;
        }
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return;
        }


    }


}
