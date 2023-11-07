package com.niuktok.backend.interactive.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.interactive.pojo.dto.UserVideoViewDTO;
import com.niuktok.backend.interactive.pojo.vo.UserVideoShareVO;
import com.niuktok.backend.interactive.service.InteractiveService;

import io.swagger.annotations.ApiOperation;

@RestController
@Validated
public class InteractiveController {
    @Autowired
    private InteractiveService interactiveService;

    @ApiOperation("用户观看视频")
    @PutMapping(value = "/op/view/{videoID}", produces = "application/json;charset=UTF-8")
    public BaseResponseVO view(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID,
        @RequestBody 
        @Valid 
        UserVideoViewDTO dto
    ) {
        interactiveService.view(userID, videoID, dto.getProgress());
        return BaseResponseVO.ok();
    }

    @ApiOperation("用户分享视频")
    @PutMapping(value = "/op/share/{videoID}", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<UserVideoShareVO> share(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        return GenericResponseVO.ok(interactiveService.share(userID, videoID));
    }

    @ApiOperation("用户点赞视频")
    @PostMapping(value = "/op/like/{videoID}", produces = "application/json;charset=UTF-8")
    public BaseResponseVO like(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        interactiveService.like(userID, videoID);
        return BaseResponseVO.ok();
    }

    @ApiOperation("用户取消点赞")
    @DeleteMapping(value = "/op/cancel/like/{videoID}", produces = "application/json;charset=UTF-8")
    public BaseResponseVO cancelLike(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        interactiveService.cancelLike(userID, videoID);
        return BaseResponseVO.ok();
    }

    @ApiOperation("用户收藏视频")
    @PostMapping(value = "/op/favorite/{videoID}", produces = "application/json;charset=UTF-8")
    public BaseResponseVO favorite(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        interactiveService.favorite(userID, videoID);
        return BaseResponseVO.ok();
    }

    @ApiOperation("用户取消收藏视频")
    @DeleteMapping(value = "/op/cancel/favorite/{videoID}", produces = "application/json;charset=UTF-8")
    public BaseResponseVO cancelFavorite(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @PathVariable("videoID")
        @NotNull(message = "视频 ID 不能为空") 
        @Positive(message = "视频 ID 不能为负数") 
        Long videoID
    ) {
        interactiveService.cancelFavorite(userID, videoID);
        return BaseResponseVO.ok();
    }
}
