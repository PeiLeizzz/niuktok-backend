package com.niuktok.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;

import io.jsonwebtoken.JwtException;

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

    private JwtTokenUtil jwtTokenUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, List<String> ignoreUrls) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.ignoreUrls = ignoreUrls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        boolean needIgnored = false;
        for (String ignoreUrl : ignoreUrls) {
            if (request.getServletPath().matches(ignoreUrl)) {
                needIgnored = true;
                break;
            }
        }
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        boolean tokenHeaderValid = tokenHeader != null && tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX);
        // 如果请求头中没有 Authorization 信息则直接拦截
        if (!needIgnored && !tokenHeaderValid) {
            BaseResponseVO responseVO = new BaseResponseVO(ResponseStatusType.UNAUTHORIZED);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseVO));
            return;
        }
        if (!tokenHeaderValid) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有 Token，则进行解析，并且设置认证信息
        // ignoreUrl 也需要设置，防止盗用它人 Token
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        try {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        } catch (NiuktokException e) {
            // BUG: 未登陆用户请求 /logout 时无法获得响应
            BaseResponseVO responseVO = new BaseResponseVO(e.getResponseStatusType());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseVO));
            return;
        }
        super.doFilterInternal(request, response, chain);
        jwtTokenUtil.renewal(token);
    }

    // 这里从 Token 中获取用户信息并设置 Authentication 到 SecurityContext 中
    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws NiuktokException {
        try {
            boolean expiration = jwtTokenUtil.isExpiration(token);
            if (expiration) {
                throw new NiuktokException(ResponseStatusType.EXPIRED_TOKEN);
            }

            String userID = jwtTokenUtil.getUserID(token);
            String role = jwtTokenUtil.getUserRole(token);
            if (userID == null) {
                throw new NiuktokException(ResponseStatusType.WRONG_TOKEN);
            }
            return new UsernamePasswordAuthenticationToken(userID, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        } catch (JwtException e) {
            throw new NiuktokException(ResponseStatusType.WRONG_TOKEN, e);
        } catch (NiuktokException e) {
            throw e;
        } catch (Exception e) {
            throw new NiuktokException(ResponseStatusType.ERROR, e);
        }
    }
}