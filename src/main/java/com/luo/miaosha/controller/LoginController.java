package com.luo.miaosha.controller;

import com.luo.miaosha.redis.MiaoshaUserKey;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.result.Result;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.service.RedisService;
import com.luo.miaosha.util.UUIDUtil;
import com.luo.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private MiaoshaUserService userService;
    @RequestMapping("/to_login")
    public String halo(Model model) {
        model.addAttribute("name", "hu");
        return "login";
    }
    @RequestMapping("/login")
    @ResponseBody
    public Result<Boolean> dbGet(HttpServletResponse resp, @Valid LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String inputPass = loginVo.getPassword();
        if (StringUtils.isEmpty(inputPass)) {
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        boolean result = userService.login(resp, loginVo);

        return Result.success(true);
    }
}
