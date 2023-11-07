package com.niuktok.backend.interactive.pojo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "观看视频 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVideoViewDTO {
    @ApiModelProperty(value = "观看进度（秒）", required = true)
    @NotNull(message = "观看进度不能为空")
    @Positive(message = "观看进度不能为负数")
    private Integer progress;
}
