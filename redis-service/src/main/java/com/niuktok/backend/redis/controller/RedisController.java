package com.niuktok.backend.redis.controller;

import com.niuktok.backend.redis.service.RedisService;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController implements RedisService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @GetMapping("/")
    public String get(@RequestParam("key") String key) {
        try {
            Object object = redisService.get(key);
            return String.valueOf(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/lock")
    public String getWithLock(@RequestParam("key") String key) {
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
}