package com.niuktok.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * mvc 配置类，主要用来做静态资源的映射
 */
@Configuration
public class WebConfigurer extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // swagger 访问配置，不单独这样配置的话就会去找本机地址
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}