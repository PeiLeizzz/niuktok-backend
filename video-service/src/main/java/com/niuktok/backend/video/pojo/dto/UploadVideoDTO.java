package com.niuktok.backend.video.pojo.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

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

    @ApiModelProperty(value = "视频标题", required = true)
    @NotBlank(message = "视频标题不能为空")
    @Length(max = 256, message = "视频标题长度不能超过 256")
    private String title;

    @ApiModelProperty("视频描述")
    @Length(max = 1024, message = "视频描述长度不能超过 1024")
    private String description;

    @ApiModelProperty(value = "分区 ID", required = true)
    @NotNull(message = "分区 ID 不能为空")
    @Positive(message = "分区 ID 不能为负数")
    private Long videoPartitionId;

    @ApiModelProperty(value = "标签集合")
    @Size(max = 10, message = "视频标签不能超过 10 个")
    private List<
        @NotBlank(message = "标签不能为空")
        @Length(max = 64, message = "标签名称长度不能超过 64")
        String> tags;
}
