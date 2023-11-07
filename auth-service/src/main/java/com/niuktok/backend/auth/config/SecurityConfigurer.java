package com.niuktok.backend.auth.config;

import com.niuktok.backend.auth.filter.JWTAuthenticationFilter;
import com.niuktok.backend.auth.filter.JWTAuthorizationFilter;
import com.niuktok.backend.auth.handler.JwtAccessDeniedHandler;
import com.niuktok.backend.auth.handler.JwtLogoutSuccessHandler;
import com.niuktok.backend.auth.handler.JwtUnauthorizedEntryPoint;
import com.niuktok.backend.auth.service.feign.RedisFeign;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.config.AuthConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthConfigurer authConfigurer;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisFeign redisFeign;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtTokenUtil))
                // token 校验需要在 logout filter 之前
                // 否则 logout handler 中获取不到 authentication
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), jwtTokenUtil, authConfigurer.getIgnoreUrls()), LogoutFilter.class)
                // 不需要 Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtUnauthorizedEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new JwtLogoutSuccessHandler(redisFeign))
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}