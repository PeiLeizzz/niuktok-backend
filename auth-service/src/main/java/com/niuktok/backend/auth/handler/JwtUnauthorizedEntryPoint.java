package com.niuktok.backend.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtUnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        BaseResponseVO responseVO = new BaseResponseVO(ResponseStatusType.UNAUTHORIZED);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}
