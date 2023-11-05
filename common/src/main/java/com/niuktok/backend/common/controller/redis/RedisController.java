package com.niuktok.backend.common.controller.redis;

import com.niuktok.backend.common.pojo.dto.redis.RedisSetDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface RedisController {
    @GetMapping(value = "/get")
    GenericResponseVO<String> get(@RequestParam("key") @NotBlank(message = "key 不能为空") String key);

    @PostMapping(value = "/set")
    BaseResponseVO set(@Valid @RequestBody RedisSetDTO redisSetDTO);

    @GetMapping(value = "/exists")
    GenericResponseVO<Boolean> exists(@RequestParam("key") @NotBlank(message = "key 不能为空") String key);

    @GetMapping(value = "/expire")
    BaseResponseVO expire(@RequestParam("key") @NotBlank(message = "key 不能为空") String key,
                   @RequestParam("seconds")
                   @NotNull(message = "过期时间（秒）不能为空")
                   @Positive(message = "过期时间（秒）不能为负数") Integer expireSeconds);

    @DeleteMapping(value = "/del")
    BaseResponseVO delete(@RequestParam("key") @NotBlank(message = "key 不能为空") String key);
}
