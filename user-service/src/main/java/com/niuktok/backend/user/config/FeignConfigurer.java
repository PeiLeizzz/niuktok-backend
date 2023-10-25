package com.niuktok.backend.user.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 拓展配置类，适配向其他服务请求的需要
 */
@Configuration
public class FeignConfigurer implements RequestInterceptor {
    /**
     * 实现 RequestInterceptor 拦截器的 apply 方法，以便能够在请求其他服务时携带 Cookie
     * Cookie 中需要存放真实、满足权限的 uid
     * 过期时间尽量设置长一点
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Cookie", "uid=1; Path=/; Expires=Sun, 29 Oct 2100 06:24:31 GMT;");
    }

    // 请求其他服务的连接时间，默认 50s，Nacos 上可动态配置
    @Value("${customize.feign.connect-timeout: 50000}")
    private int connectTimeout;
    // 请求其他服务的传输时间，默认 100s，Nacos 上可动态配置
    @Value("${customize.feign.read-timeout: 100000}")
    private int readTimeout;

    // Options Bean，用于承载超时等配置
    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }
}