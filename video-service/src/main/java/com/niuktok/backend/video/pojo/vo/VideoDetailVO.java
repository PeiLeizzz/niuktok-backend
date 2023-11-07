package com.niuktok.backend.video.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频的细节信息响应")
public class VideoDetailVO {
    @ApiModelProperty("分区信息")
    private PartitionVO.PartitionInfoVO partitionInfo;

    @ApiModelProperty("请求者与视频交互的相关信息")
    private InteractiveInfo InteractiveInfo;

    @Data
    @AllArgsConstructor
    @ApiModel(description = "请求者与视频交互的相关信息")
    public static class InteractiveInfo {
        @ApiModelProperty("是否点赞")
        private Boolean isLiked;

        @ApiModelProperty("是否分享")
        private Boolean isShared;

        @ApiModelProperty("是否收藏")
        private Boolean isFavorited;

        @ApiModelProperty("上次观看进度")
        private Integer lastProgress;

        public InteractiveInfo() {
            this.isLiked = false;
            this.isShared = false;
            this.isFavorited = false;
        }
    }   
}
