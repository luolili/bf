package com.luo.miaosha.controller;

import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.service.GoodsService;
import com.luo.miaosha.service.MiaoshaOrderService;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.service.OrderService;
import com.luo.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/miaosha_goods")
@Slf4j
public class MiaoshaItemController {

    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @RequestMapping("/to_list")
    public String itemList(Model model,
                           @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false)
                                   String cookieToken,
                           @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false)
                                   String paramToken, HttpServletResponse resp) {

        if (cookieToken == null && paramToken == null) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        MiaoshaUser user = userService.getByToken(resp, token);
        model.addAttribute("user", user);
        return "item_list";
    }


    @RequestMapping("/do_miaosha")
    public String itemList2(Model model, MiaoshaUser user, @RequestParam("goodsId") Integer goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errMsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        return "item_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String itemDetail(Model model, MiaoshaUser user, @PathVariable("goodsId") Integer goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);

        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long remainSeconds;
        Integer miaoshaStatus;
        if (now < startTime) {
            //没开始
            miaoshaStatus = 0;
            remainSeconds = startTime - now;
        } else if (now > endTime) {//finished
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }


        model.addAttribute("goodsVoList", goodsVo);
        return "item_detail";
    }

}
