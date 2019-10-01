package com.luo.miaosha.service;

import com.luo.miaosha.dao.MiaoshaUserMapper;
import com.luo.miaosha.dao.UserMapper;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.User;
import com.luo.miaosha.exception.GlobalException;
import com.luo.miaosha.redis.MiaoshaUserKey;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.util.MD5Util;
import com.luo.miaosha.util.UUIDUtil;
import com.luo.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    private RedisService redisService;
    @Autowired
    private MiaoshaUserMapper userMapper;

    public MiaoshaUser getUserById(Integer id) {
        MiaoshaUser user=userMapper.getUserById(id);
        return user;
    }

    public boolean login(HttpServletResponse resp, LoginVo loginVo) {
        if (loginVo == null) {
            //return CodeMsg.SERVER_ERROR;
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoshaUser user = getUserById(Integer.valueOf(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
       String calcPAss= MD5Util.formPassToDBPass(formPass, saltDB);
        if (!dbPass.equals(calcPAss)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
//分布式session
        //生成cookie
        addCookie(resp, user);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse resp, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(resp, user);
        }

        return user;

    }

    public void addCookie(HttpServletResponse resp, MiaoshaUser user) {
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token, token, user);
        //cookie 里面放token
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(1000);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
}
