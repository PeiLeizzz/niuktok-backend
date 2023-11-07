package com.niuktok.backend.video.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.pojo.dto.interactive.VideoInteractionSyncDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.video.pojo.vo.VideoDetailVO;
import com.niuktok.backend.video.pojo.vo.VideoPullVO;
import com.niuktok.backend.video.service.VideoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
public class VideoController implements com.niuktok.backend.common.controller.video.VideoController {
    @Autowired
    private VideoService videoService;

    @Override
    @GetMapping(value = "/internal/exist/{videoID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("查询视频是否存在")
    public GenericResponseVO<Boolean> exist(@PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID) {
        return GenericResponseVO.ok(videoService.exist(videoID));
    }

    @Override
    @PostMapping(value = "/internal/sync/interaction/{videoID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("同步视频交互数据")
    public BaseResponseVO syncInteraction(
        @PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID,
        @RequestBody @Valid VideoInteractionSyncDTO dto
    ) {
        videoService.syncInteraction(videoID, dto.getInteractiveType(), dto.getNum());
        return BaseResponseVO.ok();
    }

    @Override
    @GetMapping(value = "/internal/path/{videoID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取视频路径")
    public GenericResponseVO<String> getPath(@PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID) {
            return GenericResponseVO.ok(videoService.getPath(videoID), null);
    }

    @GetMapping(value = "/ua/pull", produces = "application/json;charset=UTF-8")
    @ApiOperation("拉取视频")
    public GenericResponseVO<VideoPullVO> pull(
        @RequestHeader(value = "userID", required = false) 
        @ApiParam(value = "用户 ID", required = false)
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "拉取数量，默认为 10，范围 [1, 20]", required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10")
        @Positive(message = "每页项数必须为大于 0 的整数")
        @Max(value = 20, message = "每次最多拉取 20 项")
        Integer size
    ) {
        List<Video> videos;
        if (userID == null) {
            videos = videoService.anonymousPull(size);
        }
        videos = videoService.userPull(userID, size);

        List<VideoPullVO.VideoInfoVO> videoList = videos.stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }

    @GetMapping(value = "/ua/detail/{videoID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取视频详细信息")
    public GenericResponseVO<VideoDetailVO> detail(
        @ApiParam(value = "用户 ID", required = false)
        @RequestHeader(value = "userID", required = false) 
        @Positive(message = "用户 ID 不能为负数") 
        Long userID,
        @ApiParam(value = "视频 ID", required = true)
        @PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        return GenericResponseVO.ok(videoService.detail(userID, videoID));
    }
}
