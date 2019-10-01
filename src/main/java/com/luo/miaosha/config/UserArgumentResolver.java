package com.luo.miaosha.config;

import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private MiaoshaUserService userService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        return MiaoshaUser.class == parameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletResponse resp = webRequest.getNativeRequest(HttpServletResponse.class);
        HttpServletRequest req = webRequest.getNativeResponse(HttpServletRequest.class);

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
        for (Cookie cookie : cookies) {
            if (cookieNameToken.equals(cookie.getName())) {
                return cookie.getValue();
            }

        }
        return null;
    }
}
