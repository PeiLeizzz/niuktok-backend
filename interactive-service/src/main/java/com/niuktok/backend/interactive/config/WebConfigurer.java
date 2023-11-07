package com.niuktok.backend.interactive.config;

import com.niuktok.backend.interactive.interceptor.VideoExistInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration("interactiveWebConfigurer")
public class WebConfigurer extends com.niuktok.backend.common.config.WebConfigurer {
    @Bean
    public VideoExistInterceptor videoExistInterceptor() {
        return new VideoExistInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(videoExistInterceptor()).addPathPatterns("/op/**");
    }
}
