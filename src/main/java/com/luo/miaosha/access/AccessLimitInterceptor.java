package com.luo.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.luo.miaosha.config.UserContext;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.redis.AccessKey;
import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.service.MiaoshaUserService;
import com.luo.miaosha.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            } else {
                int seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                boolean needLogin = accessLimit.needLogin();
                String key = request.getRequestURI();
                if (needLogin) {
                    if (user == null) {
                        render(response, CodeMsg.REPEATE_MIAOSHA);
                        return false;
                    }
                    key += "_" + user.getId();

                } else {
                    //
                }
                AccessKey accessKey = AccessKey.withExpire(seconds);
                Integer count = redisService.get(accessKey, key, Integer.class);
                if (count == null) {
                    redisService.set(accessKey, key, 1);
                } else if (count < maxCount) {
                    redisService.incr(accessKey, key);
                } else {
                    render(response, CodeMsg.ACCESS_LIMIT);
                    return false;
                }

            }
        }

        return true;
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(cm);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();

    }

    public MiaoshaUser getUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String paramToken = req.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(req, MiaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(resp, token);
    }

    private String getCookieValue(HttpServletRequest req, String cookieNameToken) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieNameToken.equals(cookie.getName())) {
                return cookie.getValue();
            }

        }
        return null;
    }
}
