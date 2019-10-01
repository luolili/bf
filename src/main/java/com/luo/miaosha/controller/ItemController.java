package com.luo.miaosha.controller;

import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.service.MiaoshaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private MiaoshaUserService userService;

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

}
