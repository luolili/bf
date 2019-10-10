package com.luo.miaosha.vo;

import com.luo.miaosha.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVO {

    private GoodsVo goodsVo;
    private OrderInfo orderInfo;
}
