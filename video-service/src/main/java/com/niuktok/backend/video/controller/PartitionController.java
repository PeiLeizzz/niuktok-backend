package com.niuktok.backend.video.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.annotation.EnumCheck;
import com.niuktok.backend.common.def.OrderType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.entity.VideoPartition;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.video.pojo.vo.PartitionVO;
import com.niuktok.backend.video.pojo.vo.VideoPullVO;
import com.niuktok.backend.video.service.PartitionService;
import com.niuktok.backend.video.service.VideoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
public class PartitionController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private PartitionService partitionService;

    @ApiOperation("获取所有视频分区")
    @GetMapping(value = "/ua/partition", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<PartitionVO> partitions() {
        List<VideoPartition> partitions = partitionService.getAllPartitions();
        List<PartitionVO.PartitionInfoVO> partitionList = partitions.stream().map(p -> 
            new PartitionVO.PartitionInfoVO(p)
        ).collect(Collectors.toList());
        return GenericResponseVO.ok(new PartitionVO(partitionList));
    }

    @ApiOperation("获取某一分区最近的视频")
    @GetMapping(value = "/ua/partition/{partitionID}", produces = "application/json;charset=UTF-8")
    public GenericResponseVO<VideoPullVO> partitionVideos(
        @ApiParam(value = "分区 ID", required = true)
        @PathVariable(value = "partitionID", required = true) 
        @NotNull(message = "分区 ID 不能为空")
        @Positive(message = "分区 ID 不能为负数") 
        Long partitionID,
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
        PageInfo<Video> videos = partitionService.getPartitionVideos(partitionID, pageNo, pageSize, orderDir);
        List<VideoPullVO.VideoInfoVO> videoList = videos.getList().stream().map(v -> {
            v.setVideoPath(videoService.concatPrivatePath(v.getVideoKey()));
            return new VideoPullVO.VideoInfoVO(v);
        }).collect(Collectors.toList());
        return GenericResponseVO.ok(new VideoPullVO(videoList));
    }
}
