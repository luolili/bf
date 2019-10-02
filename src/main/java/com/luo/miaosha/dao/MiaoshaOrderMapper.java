package com.luo.miaosha.dao;

import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaOrderMapper {

    @Select("select * from miaosha_order where id = #{id}")
    MiaoshaOrder getById(@Param("id") Integer id);

    @Select("select * from miaosha_order where user_id = #{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getByUserIdGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);


}
