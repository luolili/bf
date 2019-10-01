package com.luo.miaosha.dao;

import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderInfoMapper {

    @Select("select * from order_info where id = #{id}")
    OrderInfo getById(@Param("id") Integer id);
}
