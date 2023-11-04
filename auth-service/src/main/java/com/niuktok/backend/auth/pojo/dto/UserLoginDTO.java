package com.niuktok.backend.auth.pojo.dto;

import com.niuktok.backend.common.def.IdentityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("用户登陆请求体")
@Data
public class UserLoginDTO {
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
    private IdentityType identityType;

    @ApiModelProperty(value = "是否记住登陆状态", required = true)
    @NotNull(message = "是否记住登陆状态不能为空")
    private Boolean rememberMe;
}
