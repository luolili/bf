package com.luo.miaosha.service;

import com.luo.miaosha.dao.GoodsMapper;
import com.luo.miaosha.dao.MiaoshaGoodsMapper;
import com.luo.miaosha.dao.UserMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaGoods;
import com.luo.miaosha.domain.User;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MiaoshaGoodsMapper miaoshaGoodsMapper;

    public Goods getById(Integer id) {
        Goods goods = goodsMapper.getById(id);
        return goods;
    }

    public List<GoodsVo> getGoodsVoList() {
        List<GoodsVo> goodsVoList = goodsMapper.getGoodsVoList();
        return goodsVoList;
    }


    public GoodsVo getGoodsVoById(Integer id) {
        GoodsVo goods = goodsMapper.getGoodsVoById(id);
        return goods;
    }


    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret = miaoshaGoodsMapper.reduceStock(g);
        return ret > 0;
    }
}
