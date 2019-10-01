package com.luo.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private Integer id;
    private Integer userId;
    private Integer deliveryAddrId;
    @NotNull
    private String goodsName;
    private String goodsCount;
    private BigDecimal goodsPrice;
    private Integer goodsChannel;
    private Integer status;
    private Date startDate;
    private Date endDate;
}
