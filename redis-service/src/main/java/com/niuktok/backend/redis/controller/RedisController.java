package com.niuktok.backend.redis.controller;

import com.niuktok.backend.common.pojo.dto.redis.RedisSetDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.redis.service.RedisService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@RestController
public class RedisController implements com.niuktok.backend.common.controller.redis.RedisController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @GetMapping("/get")
    public GenericResponseVO<String> get(@RequestParam("key") @NotBlank(message = "key 不能为空") String key) {
        return GenericResponseVO.ok(String.valueOf(redisService.get(key)), "success");
    }

    @Override
    @PostMapping("/set")
    public BaseResponseVO set(@Valid @RequestBody RedisSetDTO redisSetDTO) {
        if (redisSetDTO.getExpireSeconds() == null) {
            redisService.set(redisSetDTO.getKey(), redisSetDTO.getValue());
        } else {
            redisService.set(redisSetDTO.getKey(), redisSetDTO.getValue(), redisSetDTO.getExpireSeconds());
        }
        return BaseResponseVO.ok();
    }

    @Override
    @GetMapping(value = "/exists")
    public GenericResponseVO<Boolean> exists(@RequestParam("key") @NotBlank(message = "key 不能为空") String key) {
        return GenericResponseVO.ok(redisService.exists(key));
    }

    @Override
    @GetMapping(value = "/expire")
    public BaseResponseVO expire(@RequestParam("key") @NotBlank(message = "key 不能为空") String key,
                   @RequestParam("seconds")
                   @NotNull(message = "过期时间（秒）不能为空")
                   @Positive(message = "过期时间（秒）不能为负数") Integer expireSeconds) {
        redisService.expire(key, expireSeconds);
        return BaseResponseVO.ok();
    }

    @Override
    @DeleteMapping(value = "/del")
    public BaseResponseVO delete(@RequestParam("key") @NotBlank(message = "key 不能为空") String key) {
        redisService.delete(key);
        return BaseResponseVO.ok();
    }
}