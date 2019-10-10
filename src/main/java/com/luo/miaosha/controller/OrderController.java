package com.luo.miaosha.controller;

import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.result.Result;
import com.luo.miaosha.service.GoodsService;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.service.OrderService;
import com.luo.miaosha.vo.GoodsVo;
import com.luo.miaosha.vo.OrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private OrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> itemList2(Model model, MiaoshaUser user, @RequestParam("orderId") Integer orderId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        OrderInfo orderInfo = orderService.getById(orderId);
        if (orderInfo == null) {
            model.addAttribute("errMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Integer goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);

        OrderDetailVO orderDetailVO = new OrderDetailVO();

        orderDetailVO.setOrderInfo(orderInfo);
        orderDetailVO.setGoodsVo(goodsVo);

        return Result.success(orderDetailVO);
    }


}
