package com.niuktok.backend.interactive.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "用户分享视频 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVideoShareVO {
    @ApiModelProperty(value = "分享链接", required = true)
    private String url;
}
