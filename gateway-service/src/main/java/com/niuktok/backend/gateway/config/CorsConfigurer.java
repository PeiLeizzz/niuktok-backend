package com.niuktok.backend.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfigurer {
    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.gateway.globalcors")
    public UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource(){
        return new UrlBasedCorsConfigurationSource();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = urlBasedCorsConfigurationSource();
        return new CorsWebFilter(source);
    }
}
