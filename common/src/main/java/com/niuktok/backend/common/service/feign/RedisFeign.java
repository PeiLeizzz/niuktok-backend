package com.niuktok.backend.common.service.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RedisFeign {
    @GetMapping(value = "/")
    String get(@RequestParam("key") String key);

    @GetMapping(value = "/lock")
    String getWithLock(@RequestParam("key") String key);
}
