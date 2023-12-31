package com.niuktok.backend.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.mapper.base.MyMapper;

public interface VideoMapper extends MyMapper<Video> {
    void updateInteractiveNum(@Param("videoID") Long videoID, @Param("interactiveName") String interactiveName, @Param("num") Long num);

    List<Video> selectRandom(@Param("size") Integer size, @Param("filterVideos") List<Video> videos);

    List<Video> selectUserRecentRandom(@Param("userID") Long userID, @Param("range") Long range,  @Param("size") Integer size);

    List<Video> selectRelateVideos(@Param("userID") Long userID, @Param("interactiveName") String interactiveName, @Param("orderDir") String orderDir);

    List<Video> selectVideosOfUser(@Param("userID") Long userID, @Param("orderDir") String orderDir);

    List<Video> selectPartitionVideos(@Param("partitionID") Long partitionID, @Param("orderDir") String orderDir);
}