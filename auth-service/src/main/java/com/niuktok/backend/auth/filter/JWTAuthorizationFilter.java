package com.niuktok.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 授权
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private List<String> ignoreUrls;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, List<String> ignoreUrls) {
        super(authenticationManager);
        this.ignoreUrls = ignoreUrls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        for (String ignoreUrl : ignoreUrls) {
            if (request.getServletPath().matches(ignoreUrl)) {
                chain.doFilter(request, response);
                return;
            }
        }
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 如果请求头中没有 Authorization 信息则直接拦截
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            BaseResponseVO responseVO = new BaseResponseVO(ResponseStatusType.UNAUTHORIZED);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseVO));
            return;
        }
        // 如果请求头中有 Token，则进行解析，并且设置认证信息
        try {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        } catch (NiuktokException e) {
            BaseResponseVO responseVO = new BaseResponseVO(e.getResponseStatusType());

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseVO));
            return;
        }
        super.doFilterInternal(request, response, chain);
    }

    // 这里从 Token 中获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) throws NiuktokException {
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        try {
            boolean expiration = JwtTokenUtil.isExpiration(token);
            if (expiration) {
                throw new NiuktokException(ResponseStatusType.EXPIRED_TOKEN);
            }

            String userID = JwtTokenUtil.getUserID(token);
            String role = JwtTokenUtil.getUserRole(token);
            if (userID == null) {
                throw new NiuktokException(ResponseStatusType.WRONG_TOKEN);
            }
            return new UsernamePasswordAuthenticationToken(userID, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        } catch (Exception e) {
            throw new NiuktokException(ResponseStatusType.WRONG_TOKEN, e);
        }
    }
}