package com.niuktok.backend.video.service;

import com.niuktok.backend.common.def.VideoInteractiveType;

public interface VideoService {
    boolean exist(Long videoID);
    
    void syncInteraction(Long videoID, VideoInteractiveType interactiveType, Long num);

    String getPath(Long videoID);
}
