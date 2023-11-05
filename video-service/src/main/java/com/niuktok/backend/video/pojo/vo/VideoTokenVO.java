package com.niuktok.backend.video.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "获取七牛上传 Token 响应")
public class VideoTokenVO {
    private String token;
}
