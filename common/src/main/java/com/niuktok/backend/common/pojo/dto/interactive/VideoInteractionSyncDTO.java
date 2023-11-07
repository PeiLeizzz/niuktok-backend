package com.niuktok.backend.common.pojo.dto.interactive;

import javax.validation.constraints.NotNull;

import com.niuktok.backend.common.def.VideoInteractiveType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Redis set 请求体")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoInteractionSyncDTO {
    @ApiModelProperty(value = "交互类型", dataType = "int", required = true)
    @NotNull(message = "交互类型不能为空")
    private VideoInteractiveType interactiveType;

    @ApiModelProperty(value = "交互数目", required = true)
    @NotNull(message = "交互数目不能为空")
    private Long num;
}
