package com.niuktok.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.auth.entity.AuthenticationUser;
import com.niuktok.backend.auth.pojo.dto.UserLoginDTO;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JWT 认证
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录的信息
        try {
            UserLoginDTO loginUser = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
            rememberMe.set(loginUser.getRememberMe() != null && loginUser.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getIdentityType().getCode() + ":" + loginUser.getIdentifier(), loginUser.getCredential(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        AuthenticationUser jwtUser = (AuthenticationUser) authResult.getPrincipal();
        boolean isRemember = rememberMe.get();

        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }

        String token = JwtTokenUtil.createToken(jwtUser.getUsername(), role, isRemember);
        response.setHeader(JwtTokenUtil.TOKEN_HEADER, JwtTokenUtil.TOKEN_PREFIX + token);
        BaseResponseVO responseVO = BaseResponseVO.ok("登陆成功");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        BaseResponseVO responseVO = new BaseResponseVO(ResponseStatusType.WRONG_CREDENTIAL);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (e.getMessage() != null) {
            responseVO.setMessage(responseVO.getMessage() + ": " + e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}