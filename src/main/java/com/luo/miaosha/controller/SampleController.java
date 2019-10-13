package com.luo.miaosha.controller;

import com.luo.miaosha.domain.User;
import com.luo.miaosha.rabbitmq.MQSender;
import com.luo.miaosha.redis.UserKey;
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

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public String fanout() {
        mqSender.sendFanout("halo mm");
        return "halo";
    }
    @RequestMapping("/mq")
    @ResponseBody
    public String mqTopic() {
        mqSender.sendTopic("halo mm");
        return "halo";
    }

    @RequestMapping("/mq")
    @ResponseBody
    public String mq() {
        mqSender.send("halo mm");
        return "halo";
    }
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
        Long name = redisService.get(UserKey.getById,"name", Long.class);
        return Result.success(name);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<User> redisSet(Model model) {
        boolean result = redisService.set(UserKey.getById,"name2", "ki");
        //String s=redisService.get(UserKey.getById,"name2", String.class);
        User user=new User(1,"huy");
        redisService.set(UserKey.getById, ""+user.getId(), user);
        User user1 = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user1);
    }
}
