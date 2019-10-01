package com.luo.miaosha.vo;

import com.luo.miaosha.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {
    private Integer stockCount;
    private BigDecimal miaoshaPrice;
    private Date startDate;
    private Date endDate;
}
