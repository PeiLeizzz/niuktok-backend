package com.niuktok.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.auth.entity.AuthenticationUser;
import com.niuktok.backend.auth.exception.NiuktokAuthenticationException;
import com.niuktok.backend.auth.pojo.dto.UserLoginDTO;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.IdentityType;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.utils.ValidateUtil;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * JWT 认证
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        super.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录的信息
        UserLoginDTO loginUser;
        try {
            loginUser = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not one of the values accepted for Enum class")) {
                throw new NiuktokAuthenticationException(ResponseStatusType.INVALID_PARAMS, 
                    "invalid identityType, not one of the values accepted for " + Arrays.toString(IdentityType.values()));
            }
            throw new NiuktokAuthenticationException(ResponseStatusType.INVALID_PARAMS);
        }
        
        List<String> errors = ValidateUtil.valid(loginUser);
        if (errors != null && errors.size() > 0) {
            throw new NiuktokAuthenticationException(ResponseStatusType.INVALID_PARAMS, String.join("\n", errors));
        }

        rememberMe.set(loginUser.getRememberMe() != null && loginUser.getRememberMe());
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getIdentityType().getCode() + ":" + loginUser.getIdentifier(), loginUser.getCredential(), new ArrayList<>())
        );
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

        String token = jwtTokenUtil.createToken(jwtUser.getUser().getId().toString(), role, isRemember);
        response.setHeader("Access-Control-Expose-Headers", JwtTokenUtil.TOKEN_HEADER);
        response.setHeader(JwtTokenUtil.TOKEN_HEADER, JwtTokenUtil.TOKEN_PREFIX + token);
        BaseResponseVO responseVO = BaseResponseVO.ok("登陆成功");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        BaseResponseVO responseVO;
        if (e instanceof NiuktokAuthenticationException) {
            NiuktokAuthenticationException ne = (NiuktokAuthenticationException) e;
            responseVO = new BaseResponseVO(ne.getResponseStatusType(), ne.getMessage());
        } else if (e.getCause() instanceof NiuktokException) {
            NiuktokException ne = (NiuktokException) e.getCause();
            responseVO = new BaseResponseVO(ne.getResponseStatusType(), ne.getMessage());
        } else {
            responseVO = new BaseResponseVO(ResponseStatusType.WRONG_CREDENTIAL);
            if (e.getMessage() != null) {
                responseVO.setMessage(responseVO.getMessage() + ": " + e.getMessage());
            }
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}