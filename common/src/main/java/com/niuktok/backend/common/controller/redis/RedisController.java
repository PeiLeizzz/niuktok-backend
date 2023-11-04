package com.niuktok.backend.common.controller.redis;

import com.niuktok.backend.common.pojo.dto.redis.RedisSetDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface RedisController {
    @GetMapping(value = "/get")
    String get(@RequestParam("key") @NotBlank(message = "key 不能为空") String key);

    @GetMapping(value = "/lock/get")
    String getWithLock(@RequestParam("key") @NotBlank(message = "key 不能为空") String key);

    @PostMapping(value = "/set")
    void set(@Valid @RequestBody RedisSetDTO redisSetDTO);
}
