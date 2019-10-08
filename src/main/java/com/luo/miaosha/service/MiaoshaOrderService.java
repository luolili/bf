package com.luo.miaosha.service;

import com.luo.miaosha.dao.MiaoshaOrderMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.OrderInfo;
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

    public MiaoshaOrder getByUserIdGoodsId(Integer userId, Integer goodsId) {
        MiaoshaOrder miaoshaOrder = orderMapper.getByUserIdGoodsId(userId, goodsId);
        return miaoshaOrder;
    }

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减少 库存
        goodsService.reduceStock(goods);

        OrderInfo orderInfo = orderService.createOrder(user, goods);
        MiaoshaOrder miaoshaOrder = orderMapper.getByUserIdGoodsId(user.getId(), goods.getId());
        return orderInfo;
    }



}
