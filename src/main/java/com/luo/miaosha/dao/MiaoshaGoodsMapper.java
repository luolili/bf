package com.luo.miaosha.dao;

import com.luo.miaosha.domain.MiaoshaGoods;
import com.luo.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoshaGoodsMapper {

    @Select("select * from miaosha_goods where id = #{id}")
    MiaoshaGoods getById(@Param("id") Integer id);

    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id=#{goodsId}")
    void reduceStock(MiaoshaGoods g);


}
