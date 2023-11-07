package com.niuktok.backend.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.auth.service.feign.RedisFeign;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    private RedisFeign redisFeign;

    public JwtLogoutSuccessHandler(RedisFeign redisFeign) {
        this.redisFeign = redisFeign;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userID = authentication.getPrincipal().toString();
        BaseResponseVO responseVO = redisFeign.delete(JwtTokenUtil.TOKEN_REDIS_KEY + userID);

        if (!ResponseStatusType.SUCCESS.getCode().equals(responseVO.getStatus())) {
            responseVO = BaseResponseVO.error("注销失败");
        } else {
            responseVO = BaseResponseVO.ok("注销成功");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}
