package com.niuktok.backend.gateway.service.feign;

import com.niuktok.backend.common.config.FeignConfigurer;
import com.niuktok.backend.common.controller.auth.AuthController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${customize.service.auth.service-name}", configuration = FeignConfigurer.class)
public interface AuthFeign extends AuthController {
}
