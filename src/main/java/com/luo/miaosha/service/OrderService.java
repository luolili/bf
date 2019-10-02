package com.luo.miaosha.service;

import com.luo.miaosha.dao.GoodsMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public Goods getById(Integer id) {
        //Goods goods = goodsMapper.getById(id);

        return null;
    }

}
