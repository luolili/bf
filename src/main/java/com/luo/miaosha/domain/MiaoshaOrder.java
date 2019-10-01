package com.luo.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiaoshaOrder {
    private Integer id;
    private Integer goodsId;
    private Integer userId;
    private Integer orderId;
}
