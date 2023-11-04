package com.niuktok.backend.redis.controller;

import com.niuktok.backend.common.pojo.dto.redis.RedisSetDTO;
import com.niuktok.backend.redis.service.RedisService;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
public class RedisController implements com.niuktok.backend.common.controller.redis.RedisController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @GetMapping("/get")
    public String get(@RequestParam("key") @NotBlank(message = "key 不能为空") String key) {
        return String.valueOf(redisService.get(key));
    }

    @Override
    @GetMapping("/lock/get")
    public String getWithLock(@RequestParam("key") @NotBlank(message = "key 不能为空") String key) {
        RLock lock = redissonClient.getLock("test-key");
        try {
            lock.lock();
            Thread.sleep(3000);
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    @PostMapping("/set")
    public void set(@Valid @RequestBody RedisSetDTO redisSetDTO) {
        redisService.set(redisSetDTO.getKey(), redisSetDTO.getValue());
    }
}