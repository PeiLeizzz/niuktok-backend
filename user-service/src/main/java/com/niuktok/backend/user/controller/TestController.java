package com.niuktok.backend.user.controller;

import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.mapper.UserMapper;
import com.niuktok.backend.user.service.feign.RedisService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-service")
public class TestController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;
    @GetMapping("/test")
    @ApiOperation("测试 swagger")
    public List<User> test() {
        return userMapper.selectAll();
    }

    @GetMapping("/redis")
    @ApiOperation("测试 redis")
    public String redis() {
        return redisService.get("test");
    }

    @GetMapping("/redission")
    @ApiOperation("测试 redission")
    public String redission() {
        return redisService.getWithLock("test");
    }
}
