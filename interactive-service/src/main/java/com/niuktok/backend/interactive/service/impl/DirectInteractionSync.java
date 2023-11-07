package com.niuktok.backend.interactive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.pojo.dto.interactive.VideoInteractionSyncDTO;
import com.niuktok.backend.interactive.service.InteractionSync;
import com.niuktok.backend.interactive.service.feign.VideoFeign;

@Service
public class DirectInteractionSync implements InteractionSync {
    @Autowired
    private VideoFeign videoFeign;

    @Override
    public void syncViewNum(Long videoID, Long num) {
        videoFeign.syncInteraction(videoID, new VideoInteractionSyncDTO(VideoInteractiveType.VIEW, num));
    }

    @Override
    public void syncLikeNum(Long videoID, Long num) {
        videoFeign.syncInteraction(videoID, new VideoInteractionSyncDTO(VideoInteractiveType.LIKE, num));
    }

    @Override
    public void syncFavoriteNum(Long videoID, Long num) {
        videoFeign.syncInteraction(videoID, new VideoInteractionSyncDTO(VideoInteractiveType.FAVORITE, num));
    }

    @Override
    public void syncShareNum(Long videoID, Long num) {
        videoFeign.syncInteraction(videoID, new VideoInteractionSyncDTO(VideoInteractiveType.SHARE, num));
    }
    
}
