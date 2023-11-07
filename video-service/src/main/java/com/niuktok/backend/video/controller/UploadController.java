package com.niuktok.backend.video.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.video.pojo.dto.UploadVideoDTO;
import com.niuktok.backend.video.pojo.vo.VideoTokenVO;
import com.niuktok.backend.video.service.UploadService;

import io.swagger.annotations.ApiOperation;

@RestController
@Validated
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @GetMapping(value = "/upload/token", produces = "application/json;charset=UTF-8")
    @ApiOperation("获取七牛 Token")
    public GenericResponseVO<VideoTokenVO> getQiniuToken() {
        return GenericResponseVO.ok(new VideoTokenVO(uploadService.getQiniuToken()));
    }

    @PostMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    @ApiOperation("上传视频")
    public BaseResponseVO uploadVideo(
        @RequestHeader("userID") 
        @NotNull(message = "userID 不能为空") 
        @Positive(message = "userID 不能为负数") 
        Long userID,
        @RequestBody
        @Valid
        UploadVideoDTO uploadVideoDTO
    ) {
        uploadService.uploadVideo(userID, uploadService.getFileInfo(uploadVideoDTO.getKey()),
            uploadVideoDTO.getKey(), uploadVideoDTO.getTitle(), uploadVideoDTO.getDescription(), 
            uploadVideoDTO.getVideoPartitionId(), uploadVideoDTO.getTags());
        return BaseResponseVO.ok();
    }
}