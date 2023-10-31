package com.niuktok.backend.user.controller;

import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.user.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.user.pojo.vo.UserInfoVO;
import com.niuktok.backend.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    @ApiOperation("用户注册")
    public BaseResponseVO register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return BaseResponseVO.ok();
    }

    @GetMapping(value = "/info/{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取用户基本信息")
    public GenericResponseVO<UserInfoVO> getUserInfo(@PathVariable(value = "id", required = true)
                                                     @Min(value = 0, message = "用户 ID 不能为负数")
                                                     Integer userID) {
        return GenericResponseVO.ok(userService.getUserInfo(userID));
    }
}
