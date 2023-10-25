package com.niuktok.backend.user.service.feign;

import com.niuktok.backend.common.service.feign.RedisFeign;
import com.niuktok.backend.user.config.FeignConfigurer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${customize.service.redis.service-name}", path = "${customize.service.redis.path-prefix}", configuration = FeignConfigurer.class)
public interface RedisService extends RedisFeign {
}
