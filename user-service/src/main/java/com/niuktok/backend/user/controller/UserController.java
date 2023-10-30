package com.niuktok.backend.user.controller;

import com.niuktok.backend.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("test")
    @ApiOperation("test")
    public String test() {
        return null;
    }
}
