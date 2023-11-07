package com.niuktok.backend.video.service;

import java.util.List;

import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.video.pojo.vo.VideoDetailVO;

public interface VideoService {
    boolean exist(Long videoID);
    
    void syncInteraction(Long videoID, Byte interactiveType, Long num);

    String getPath(Long videoID);

    String concatPrivatePath(String key);

    List<Video> anonymousPull(Integer size);

    List<Video> userPull(Long userID, Integer size);

    VideoDetailVO detail(Long userID, Long videoID);
}
