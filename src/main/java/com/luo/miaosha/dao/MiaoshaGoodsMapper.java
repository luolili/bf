package com.luo.miaosha.dao;

import com.luo.miaosha.domain.MiaoshaGoods;
import com.luo.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaGoodsMapper {

    @Select("select * from miaosha_goods where id = #{id}")
    MiaoshaGoods getById(@Param("id") Integer id);
}
