package com.luo.miaosha.service;

import com.luo.miaosha.dao.GoodsMapper;
import com.luo.miaosha.dao.UserMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.User;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public Goods getUserById(Integer id) {
        Goods goods = goodsMapper.getById(id);
        return goods;
    }

    public List<GoodsVo> getGoodsVoList() {
        List<GoodsVo> goodsVoList = goodsMapper.getGoodsVoList();
        return goodsVoList;
    }

}
