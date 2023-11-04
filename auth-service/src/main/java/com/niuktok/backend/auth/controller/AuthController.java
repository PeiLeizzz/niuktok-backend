package com.niuktok.backend.auth.controller;

import com.niuktok.backend.auth.service.AuthService;
import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    @ApiOperation("用户注册")
    public BaseResponseVO register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authService.register(userRegisterDTO);
        return BaseResponseVO.ok();
    }

    @GetMapping("test")
    public BaseResponseVO test() {
        return BaseResponseVO.ok();
    }
}
