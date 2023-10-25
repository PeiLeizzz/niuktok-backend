package com.niuktok.backend.user.controller;

import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.mapper.UserMapper;
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
    @GetMapping("/test")
    public List<User> test() {
        return userMapper.selectAll();
    }
}
