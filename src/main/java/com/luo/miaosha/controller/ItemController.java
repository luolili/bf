package com.luo.miaosha.controller;

import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.redis.GoodsKey;
import com.luo.miaosha.service.GoodsService;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.service.RedisService;
import com.luo.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


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


    @RequestMapping("/to_list2")
    public String itemList2(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsVoList", goodsVoList);
        return "item_list";
    }

    // 页面静态化
    @RequestMapping(value = "/to_list3", produces = "text/html")
    @ResponseBody
    public String itemList3(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsVoList", goodsVoList);
        //return "item_list";

        //手动渲染
        //SpringWebContext ctx = new SpringWebContext(req, resp);
        html = thymeleafViewResolver.getTemplateEngine().process("item_list", null);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;

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
