package com.niuktok.backend.common.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign 拓展配置类，适配向其他服务请求的需要
 */
@Configuration
public class FeignConfigurer implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 当主线程的请求执行完毕后，Servlet 容器会被销毁当前的 Servlet，因此在这里需要做判空
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                // 不能把所有消息头都传递下去，否则会引起其他异常；header 的 name 都是小写
                if (name.equals("Authorization")) {
                    requestTemplate.header(name, request.getHeader(name));
                }
            }
        }
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