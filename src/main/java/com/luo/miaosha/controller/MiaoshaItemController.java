package com.luo.miaosha.controller;

import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.rabbitmq.MQSender;
import com.luo.miaosha.rabbitmq.MiaoshaMessage;
import com.luo.miaosha.redis.GoodsKey;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.result.Result;
import com.luo.miaosha.service.*;
import com.luo.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/miaosha_goods")
@Slf4j
public class MiaoshaItemController implements InitializingBean {

    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    /**
     * 接口优化：减少数据库访问
     * 1.把商品库存放入 redis
     * 2.redis 预减少库存
     * 3.放入mq
     *
     */
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
    @ResponseBody
    public Result<Integer> itemList2(Model model, MiaoshaUser user, @RequestParam("goodsId") Integer goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            // return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //预减少库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, goodsId + "");
        if (stock < 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {//秒杀到了
            model.addAttribute("errMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            // return "miaosha_fail";
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(message);
        return Result.success(0);//排队中
        /*List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errMsg", CodeMsg.MIAOSHA_OVER.getMsg());
            //return "miaosha_fail";
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }*/
       /* MiaoshaOrder miaoshaOrder = miaoshaOrderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            // return "miaosha_fail";
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        OrderInfo orderInfo = miaoshaOrderService.miaosha(user, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        //return "item_list";
        return Result.success(orderInfo);*/
    }

    //系统初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, goodsVo.getId() + "", goodsVo.getStockCount());
        }

    }

    //秒杀结果
    @GetMapping("/result")
    @ResponseBody
    public Result<Integer> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId") Integer goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Integer result = miaoshaOrderService.miaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
