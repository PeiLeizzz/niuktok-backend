package com.niuktok.backend.common.mapper;

import org.apache.ibatis.annotations.Param;

import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.mapper.base.MyMapper;

public interface VideoMapper extends MyMapper<Video> {
    void updateInteractiveNum(@Param("videoID") Long videoID, @Param("columnName") String columnName, @Param("num") Long num);
}