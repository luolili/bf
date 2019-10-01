package com.luo.miaosha.dao;

import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserMapper {

    @Select("select * from user where id = #{id}")
    MiaoshaUser getUserById(@Param("id") Integer id);
}
