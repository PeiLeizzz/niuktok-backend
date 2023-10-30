package com.niuktok.backend.user.service.feign;

import com.niuktok.backend.common.controller.redis.RedisController;
import com.niuktok.backend.common.config.FeignConfigurer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${customize.service.redis.service-name}", configuration = FeignConfigurer.class)
public interface RedisFeign extends RedisController {
}
