package com.niuktok.backend.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "customize.service.auth")
public class AuthConfigurer {
    private List<String> ignoreUrls;

    private List<String> internalUrls;

    private String serviceName;

    @Value("${customize.service.auth.entry-point.url}")
    private String entryPointUrl;
}
