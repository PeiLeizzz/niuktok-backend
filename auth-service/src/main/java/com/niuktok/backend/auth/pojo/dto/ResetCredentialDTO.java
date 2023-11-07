package com.niuktok.backend.auth.pojo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.niuktok.backend.common.annotation.EnumCheck;
import com.niuktok.backend.common.def.IdentityType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改密码请求体")
public class ResetCredentialDTO {
    @ApiModelProperty(value = "登陆标识", required = true)
    @NotBlank(message = "登陆标识不能为空")
    @Length(max = 255, message = "登陆标识长度不能大于 255")
    private String identifier;

    @ApiModelProperty(value = "新密码凭证", required = true)
    @NotBlank(message = "新密码凭证不能为空")
    @Length(min = 8, max = 64, message = "新密码凭证长度范围为 8-64")
    private String credential;

    @ApiModelProperty(value = "登陆标识类型", dataType = "int", required = true)
    @NotNull(message = "登陆标识类型不能为空")
    @EnumCheck(clazz = IdentityType.class, message = "登陆标识类型不合法")
    private Byte identityType;
}