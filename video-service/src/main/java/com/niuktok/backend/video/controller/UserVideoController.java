package com.niuktok.backend.video.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.annotation.EnumCheck;
import com.niuktok.backend.common.def.OrderType;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.video.pojo.vo.VideoPullVO;
import com.niuktok.backend.video.service.UserVideoService;
import com.niuktok.backend.video.service.VideoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
public class UserVideoController {
    @Autowired
    private UserVideoService userVideoService;

    @Autowired
    private VideoService videoService;

    @ApiOperation("获取用户最近发布的视频")
    @GetMapping(value = "/mime", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> mime(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "当前页码，默认为 1，必须 > 0")
        @RequestParam(value = "pageNo", required = false, defaultValue = "1")
        @Positive(message = "页码必须为大于 0 的整数")
        Integer pageNo,
        @ApiParam(value = "当前页项数，默认为 9，必须 > 0")
        @RequestParam(value = "pageSize", required = false, defaultValue = "9")
        @Positive(message = "每页项数必须为大于 0 的整数")
        Integer pageSize,
        @ApiParam(value = "按时间排序方向，只支持 asc / desc，默认 desc", required = false)
        @RequestParam(value = "orderDir", required = false, defaultValue = "desc")
        @EnumCheck(message = "排序类型不合法", clazz = OrderType.class, method = "getDescription")
        String orderDir
    ) {
        PageInfo<Video> videos = userVideoService.getVideosOfUser(userID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }

    @ApiOperation("获取用户最近观看的视频")
    @GetMapping(value = "/views", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> view(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "当前页码，默认为 1，必须 > 0")
        @RequestParam(value = "pageNo", required = false, defaultValue = "1")
        @Positive(message = "页码必须为大于 0 的整数")
        Integer pageNo,
        @ApiParam(value = "当前页项数，默认为 9，必须 > 0")
        @RequestParam(value = "pageSize", required = false, defaultValue = "9")
        @Positive(message = "每页项数必须为大于 0 的整数")
        Integer pageSize,
        @ApiParam(value = "按时间排序方向，只支持 asc / desc，默认 desc", required = false)
        @RequestParam(value = "orderDir", required = false, defaultValue = "desc")
        @EnumCheck(message = "排序类型不合法", clazz = OrderType.class, method = "getDescription")
        String orderDir
    ) {
        PageInfo<Video> videos = userVideoService.getRelateVideos(VideoInteractiveType.VIEW, userID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }

    @ApiOperation("获取用户最近点赞的视频")
    @GetMapping(value = "/likes", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> likes(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "当前页码，默认为 1，必须 > 0")
        @RequestParam(value = "pageNo", required = false, defaultValue = "1")
        @Positive(message = "页码必须为大于 0 的整数")
        Integer pageNo,
        @ApiParam(value = "当前页项数，默认为 9，必须 > 0")
        @RequestParam(value = "pageSize", required = false, defaultValue = "9")
        @Positive(message = "每页项数必须为大于 0 的整数")
        Integer pageSize,
        @ApiParam(value = "按时间排序方向，只支持 asc / desc，默认 desc", required = false)
        @RequestParam(value = "orderDir", required = false, defaultValue = "desc")
        @EnumCheck(message = "排序类型不合法", clazz = OrderType.class, method = "getDescription")
        String orderDir
    ) {
        PageInfo<Video> videos = userVideoService.getRelateVideos(VideoInteractiveType.LIKE, userID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }

    @ApiOperation("获取用户最近收藏的视频")
    @GetMapping(value = "/favorites", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> favorites(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "当前页码，默认为 1，必须 > 0")
        @RequestParam(value = "pageNo", required = false, defaultValue = "1")
        @Positive(message = "页码必须为大于 0 的整数")
        Integer pageNo,
        @ApiParam(value = "当前页项数，默认为 9，必须 > 0")
        @RequestParam(value = "pageSize", required = false, defaultValue = "9")
        @Positive(message = "每页项数必须为大于 0 的整数")
        Integer pageSize,
        @ApiParam(value = "按时间排序方向，只支持 asc / desc，默认 desc", required = false)
        @RequestParam(value = "orderDir", required = false, defaultValue = "desc")
        @EnumCheck(message = "排序类型不合法", clazz = OrderType.class, method = "getDescription")
        String orderDir
    ) {
        PageInfo<Video> videos = userVideoService.getRelateVideos(VideoInteractiveType.FAVORITE, userID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }

    @ApiOperation("获取用户最近分享的视频")
    @GetMapping(value = "/shares", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> shares(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @ApiParam(value = "当前页码，默认为 1，必须 > 0")
        @RequestParam(value = "pageNo", required = false, defaultValue = "1")
        @Positive(message = "页码必须为大于 0 的整数")
        Integer pageNo,
        @ApiParam(value = "当前页项数，默认为 9，必须 > 0")
        @RequestParam(value = "pageSize", required = false, defaultValue = "9")
        @Positive(message = "每页项数必须为大于 0 的整数")
        Integer pageSize,
        @ApiParam(value = "按时间排序方向，只支持 asc / desc，默认 desc", required = false)
        @RequestParam(value = "orderDir", required = false, defaultValue = "desc")
        @EnumCheck(message = "排序类型不合法", clazz = OrderType.class, method = "getDescription")
        String orderDir
    ) {
        PageInfo<Video> videos = userVideoService.getRelateVideos(VideoInteractiveType.SHARE, userID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }
}
