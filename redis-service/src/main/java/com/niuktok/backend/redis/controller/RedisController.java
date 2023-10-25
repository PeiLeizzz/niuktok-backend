package com.niuktok.backend.redis.controller;

import com.niuktok.backend.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis-service")
public class RedisController implements RedisService {

    @Autowired
    private RedisService redisService;

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
}