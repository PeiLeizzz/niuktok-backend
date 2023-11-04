package com.niuktok.backend.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = "com.niuktok.backend")
@EnableDiscoveryClient
@EnableWebSecurity
@EnableFeignClients
@EnableTransactionManagement
@MapperScan({"com.niuktok.backend.auth.mapper",
        "com.niuktok.backend.common.mapper"})
// TODO: security 和 swagger 有冲突，目前文档页面仍旧加载不出来
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
