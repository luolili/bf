package com.luo.miaosha.controller;

import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.result.Result;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result<Boolean> dbGet(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String inputPass = loginVo.getPassword();
        if (StringUtils.isEmpty(inputPass)) {
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(mobile)) {
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        CodeMsg cm = userService.login(loginVo);
        if (cm.getCode() == 0) {
            return Result.success(true);
        }
        else {
            return Result.error(cm);
        }
    }
}
