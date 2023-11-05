package com.niuktok.backend.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "用户基本信息")
public class UserInfoVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登陆信息列表")
    private List<UserAuthVO> userAuthList;
}
