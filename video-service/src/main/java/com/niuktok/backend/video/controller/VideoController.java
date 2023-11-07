package com.niuktok.backend.video.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niuktok.backend.common.pojo.dto.interactive.VideoInteractionSyncDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.video.service.VideoService;

import io.swagger.annotations.ApiOperation;

@RestController
@Validated
public class VideoController implements com.niuktok.backend.common.controller.video.VideoController {
    @Autowired
    private VideoService videoService;

    @Override
    @GetMapping(value = "/ua/exist/{videoID}", produces = "application/json;charset=UTF-8")
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
    @GetMapping(value = "/ua/path/{videoID}", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取视频路径")
    public GenericResponseVO<String> getPath(@PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID) {
            return GenericResponseVO.ok(videoService.getPath(videoID), null);
    }
}
