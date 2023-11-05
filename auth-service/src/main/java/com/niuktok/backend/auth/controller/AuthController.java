package com.niuktok.backend.auth.controller;

import com.niuktok.backend.auth.service.AuthService;
import com.niuktok.backend.auth.pojo.dto.ResetCredentialDTO;
import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@Validated
public class AuthController implements com.niuktok.backend.common.controller.auth.AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    @ApiOperation("用户注册")
    public BaseResponseVO register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authService.register(userRegisterDTO);
        return BaseResponseVO.ok();
    }

    @Override
    @RequestMapping(value = "${customize.service.auth.entry-point.prefix}")
    public GenericResponseVO<String> auth(Principal principal) {
        return GenericResponseVO.ok(principal.getName(), null);
    }

    @PostMapping(value = "/modify/credential", produces = "application/json;charset=UTF-8")
    @ApiOperation("修改用户密码")
    public BaseResponseVO resetCredential(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @RequestBody @Valid ResetCredentialDTO resetCredentialDTO) {
        authService.resetCredential(userID, resetCredentialDTO.getIdentifier(), resetCredentialDTO.getCredential(), resetCredentialDTO.getIdentityType());
        return BaseResponseVO.ok();
    }
}
