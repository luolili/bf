package com.luo.miaosha.controller;

import com.luo.miaosha.domain.User;
import com.luo.miaosha.result.Result;
import com.luo.miaosha.service.RedisService;
import com.luo.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;
    @RequestMapping("/thmeleaf")
    public String halo(Model model) {
        model.addAttribute("name", "hu");
        return "halo";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<User> dbGet(Model model) {
        model.addAttribute("name", "hu");
        User user = userService.getUserById(1);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet(Model model) {
        Long name = redisService.get("name", Long.class);
        return Result.success(name);
    }
}
