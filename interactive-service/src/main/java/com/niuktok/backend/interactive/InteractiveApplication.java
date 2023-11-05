package com.niuktok.backend.interactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = "com.niuktok.backend")
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan({"com.niuktok.backend.interactive.mapper",
        "com.niuktok.backend.common.mapper"})
public class InteractiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(InteractiveApplication.class, args);
    }
}