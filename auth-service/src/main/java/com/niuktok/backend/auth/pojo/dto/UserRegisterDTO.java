package com.niuktok.backend.auth.pojo.dto;

import com.niuktok.backend.common.annotation.EnumCheck;
import com.niuktok.backend.common.def.IdentityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@ApiModel(description = "用户注册请求体")
public class UserRegisterDTO {
    @ApiModelProperty(value = "用户名，唯一", required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(max = 255, message = "用户名长度不能大于 255")
    private String username;

    @ApiModelProperty(value = "登陆标识", required = true)
    @NotBlank(message = "登陆标识不能为空")
    @Length(max = 255, message = "登陆标识长度不能大于 255")
    private String identifier;

    @ApiModelProperty(value = "密码凭证", required = true)
    @NotBlank(message = "密码凭证不能为空")
    @Length(min = 8, max = 64, message = "密码凭证长度范围为 8-64")
    private String credential;

    @ApiModelProperty(value = "登陆标识类型", dataType = "int", required = true)
    @NotNull(message = "登陆标识类型不能为空")
    @EnumCheck(clazz = IdentityType.class, message = "登陆标识类型不合法")
    private Byte identityType;
}
