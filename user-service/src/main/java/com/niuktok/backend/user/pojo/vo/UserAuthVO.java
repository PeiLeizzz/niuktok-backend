package com.niuktok.backend.user.pojo.vo;

import com.niuktok.backend.common.def.IdentityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "用户认证信息")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthVO {
    @ApiModelProperty("登陆凭证类型")
    private IdentityType identityType;

    @ApiModelProperty("登陆凭证")
    private String identifier;
}
