package com.niuktok.backend.user.controller;

import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.user.pojo.vo.UserInfoVO;
import com.niuktok.backend.user.service.UserService;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/info", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取用户基本信息")
    public GenericResponseVO<UserInfoVO> getUserInfo(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID) {
        return GenericResponseVO.ok(userService.getUserInfo(userID, true));
    }

    @GetMapping(value = "/info/{userID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取用户基本信息")
    public GenericResponseVO<UserInfoVO> getUserInfo(
        @RequestHeader("userID")
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为空")
        Long otherUserID
        ) {
        return GenericResponseVO.ok(userService.getUserInfo(userID, userID == otherUserID));
    }
}
