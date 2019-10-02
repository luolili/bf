package com.luo.miaosha.service;

import com.luo.miaosha.dao.MiaoshaOrderMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderMapper orderMapper;

    public MiaoshaOrder getByUserIdGoodsId(Integer userId, Integer goodsId) {
        MiaoshaOrder miaoshaOrder = orderMapper.getByUserIdGoodsId(userId, goodsId);
        return miaoshaOrder;
    }


}
