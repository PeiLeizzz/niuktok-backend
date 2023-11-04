package com.niuktok.backend.common.pojo.dto.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@ApiModel(description = "Redis set 请求体")
@NoArgsConstructor
@AllArgsConstructor
public class RedisSetDTO {
    @ApiModelProperty(value = "key", required = true)
    @NotBlank(message = "key 不能为空")
    private String key;

    @ApiModelProperty(value = "value", required = true)
    @NotNull(message = "value 不能为 null")
    private Object value;
}
