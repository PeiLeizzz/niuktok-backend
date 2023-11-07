package com.niuktok.backend.video.pojo.vo;

import java.util.List;

import com.niuktok.backend.common.entity.VideoPartition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分区列表响应")
public class PartitionVO {
    @ApiModelProperty("分区列表")
    private List<PartitionInfoVO> partitions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "视频基本信息")
    public static class PartitionInfoVO {
        @ApiModelProperty("分区 ID")
        private Long id;

        @ApiModelProperty("分区名称")
        private String name;

        public PartitionInfoVO(VideoPartition p) {
            this.id = p.getId();
            this.name = p.getName();
        }
    }
}
