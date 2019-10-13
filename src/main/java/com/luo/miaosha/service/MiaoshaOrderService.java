package com.luo.miaosha.service;

import com.luo.miaosha.dao.MiaoshaOrderMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.redis.MiaoshaKey;
import com.luo.miaosha.redis.OrderKey;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderMapper orderMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    public MiaoshaOrder getByUserIdGoodsId(Integer userId, Integer goodsId) {
        MiaoshaOrder order = redisService.get(OrderKey.getMiaoshaORderByUidGid, "" + userId + ":" + goodsId, MiaoshaOrder.class);
        if (order != null) {
            return order;
        }
        MiaoshaOrder miaoshaOrder = orderMapper.getByUserIdGoodsId(userId, goodsId);
        redisService.set(OrderKey.getMiaoshaORderByUidGid, "" + userId + ":" + goodsId, miaoshaOrder);
        return miaoshaOrder;
    }

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减少 库存
        boolean success = goodsService.reduceStock(goods);

        if (success) {
            //同一个 用户可以 发送2个请求，秒杀2个同样的goods，创建2个订单
            //解决方法：卖超：创建user_id,goods_id的唯一索引在miaosha_order
            return orderService.createOrder(user, goods);
        }

        setGoodsOver(goods.getId());
        return null;
    }

    private void setGoodsOver(Integer goodsId) {
        redisService.set(MiaoshaKey.isGoodsOVer, "" + goodsId, true);
    }


    public Integer miaoshaResult(Integer userId, Integer goodsId) {
        MiaoshaOrder miaoshaOrder = getByUserIdGoodsId(userId, goodsId);
        if (miaoshaOrder != null) {
            return miaoshaOrder.getId();//描述成功
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;//秒杀失败
            } else {
                return 0;//排队中
            }
        }
        //return miaoshaOrder.getId();//描述成功
    }

    private boolean getGoodsOver(Integer goodsId) {

        return redisService.exist(MiaoshaKey.isGoodsOVer, goodsId + "");

    }
}
