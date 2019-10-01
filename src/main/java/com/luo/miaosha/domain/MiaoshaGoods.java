package com.luo.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiaoshaGoods {
    private Integer id;
    private Integer goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
