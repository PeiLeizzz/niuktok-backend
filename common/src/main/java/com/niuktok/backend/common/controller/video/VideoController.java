package com.niuktok.backend.common.controller.video;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.niuktok.backend.common.pojo.dto.interactive.VideoInteractionSyncDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;

@Validated
public interface VideoController {
    @GetMapping(value = "/internal/exist/{videoID}", produces = "application/json;charset=UTF-8")
    GenericResponseVO<Boolean> exist(@PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID);

    @PostMapping(value = "/internal/sync/interaction/{videoID}", produces = "application/json;charset=UTF-8")
    BaseResponseVO syncInteraction(
        @PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID,
        @RequestBody @Valid VideoInteractionSyncDTO interactionSyncDTO);

    @GetMapping(value = "/internal/path/{videoID}", produces = "application/json;charset=UTF-8")
    GenericResponseVO<String> getPath(@PathVariable("videoID") 
        @NotNull(message = "视频 ID 不能为空") @Positive(message = "视频 ID 不能为负数") Long videoID);
}
