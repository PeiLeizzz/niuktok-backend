package com.niuktok.backend.common.controller.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RedisController {
    @GetMapping(value = "/")
    String get(@RequestParam("key") String key);

    @GetMapping(value = "/lock")
    String getWithLock(@RequestParam("key") String key);
}
