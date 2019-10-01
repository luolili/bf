package com.luo.miaosha.dao;

import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaOrderMapper {

    @Select("select * from miaosha_order where id = #{id}")
    MiaoshaOrder getUserById(@Param("id") Integer id);
}
