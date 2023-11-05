package com.niuktok.backend.video.pojo.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "上传视频")
public class UploadVideoDTO {
    @ApiModelProperty(value = "七牛视频 key", required = true)
    @NotBlank(message = "七牛视频 key 不能为空")
    private String key;

    @ApiModelProperty("视频描述")
    private String description;
}
