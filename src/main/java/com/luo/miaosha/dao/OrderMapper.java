package com.luo.miaosha.dao;

import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("select * from order_info where id = #{id}")
    OrderInfo getById(@Param("id") Integer id);

    @Select("select g.*, mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    List<GoodsVo> getGoodsVoList();

    @Select("select g.*, mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id where goods_id=#{id}")
    GoodsVo getGoodsVoById(@Param("id") Integer id);


}
