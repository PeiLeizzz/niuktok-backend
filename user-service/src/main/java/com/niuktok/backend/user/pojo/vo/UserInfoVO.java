package com.niuktok.backend.user.pojo.vo;

import com.niuktok.backend.common.def.IdentifierType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户基本信息")
public class UserInfoVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登陆标识类型")
    private IdentifierType identifierType;
}
