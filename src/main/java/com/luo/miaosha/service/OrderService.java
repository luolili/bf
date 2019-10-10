package com.luo.miaosha.service;

import com.luo.miaosha.dao.GoodsMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public OrderInfo getById(Integer id) {
        OrderInfo orderInfo = orderMapper.getById(id);

        return orderInfo;
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsChannel(1);
        orderInfo.setStatus(0);
        Integer orderId = orderMapper.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(orderInfo.getGoodsId());
        miaoshaOrder.setUserId(orderInfo.getUserId());
        miaoshaOrder.setGoodsId(orderInfo.getGoodsId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;

    }


}
