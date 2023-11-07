package com.niuktok.backend.video.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.entity.VideoPartition;

public interface PartitionService {
    List<VideoPartition> getAllPartitions();

    Boolean exist(Long partitionID);

    PageInfo<Video> getPartitionVideos(Long partitionID, Integer pageNo, Integer pageSize, String orderDir);
}
