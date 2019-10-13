package com.luo.miaosha.rabbitmq;

import com.luo.miaosha.domain.MiaoshaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {

    private MiaoshaUser user;
    private Integer goodsId;
}
