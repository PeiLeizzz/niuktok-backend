package com.niuktok.backend.video.pojo.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.video.entity.QiniuVideoInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频拉取响应")
public class VideoPullVO {
    @ApiModelProperty("视频列表")
    private List<VideoInfoVO> videos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "视频基本信息")
    public static class VideoInfoVO {
        @ApiModelProperty("视频 ID")
        private Long id;

        @ApiModelProperty("视频作者 ID")
        private Long userId;

        @ApiModelProperty("视频路径")
        private String videoPath;

        @ApiModelProperty("视频封面路径")
        private String coverPath;

        @ApiModelProperty("视频标题")
        private String title;

        @ApiModelProperty("视频描述")
        private String description;

        @ApiModelProperty("视频播放量")
        private Long viewNum;

        @ApiModelProperty("视频点赞量")
        private Long likeNum;

        @ApiModelProperty("视频收藏量")
        private Long favoriteNum;

        @ApiModelProperty("视频分享量")
        private Long shareNum;

        @ApiModelProperty("视频格式")
        private String mimeType;

        @ApiModelProperty("视频大小")
        private Long fsize;

        @ApiModelProperty("视频发布时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createdTime;

        @ApiModelProperty("视频修改时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date updatedTime;

        public VideoInfoVO(Video video) {
            this.id = video.getId();
            this.userId = video.getUserId();
            this.videoPath = video.getVideoPath();
            this.coverPath = video.getCoverPath();
            this.title = video.getTitle();
            this.description = video.getDescription();
            this.viewNum = video.getViewNum();
            this.likeNum = video.getLikeNum();
            this.favoriteNum = video.getFavoriteNum();
            this.shareNum = video.getShareNum();
            this.createdTime = video.getCreatedTime();
            this.updatedTime = video.getUpdatedTime();

            QiniuVideoInfo qiniuVideoInfo = JSON.parseObject(video.getInfo(), QiniuVideoInfo.class);
            this.mimeType = qiniuVideoInfo.getMimeType();
            this.fsize = qiniuVideoInfo.getFsize();
        }
    }
}