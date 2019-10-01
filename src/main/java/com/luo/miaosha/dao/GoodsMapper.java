package com.luo.miaosha.dao;

import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.User;
import com.luo.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("select * from goods where id = #{id}")
    Goods getById(@Param("id") Integer id);

    @Select("select g.*, mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    List<GoodsVo> getGoodsVoList();
}
