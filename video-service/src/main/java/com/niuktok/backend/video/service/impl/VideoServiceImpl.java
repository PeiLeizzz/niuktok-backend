package com.niuktok.backend.video.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.video.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public boolean exist(Long videoID) {
        Video video = new Video();
        video.setId(videoID);
        video.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        return videoMapper.selectCount(video) == 1;
    }

    @Override
    public void syncInteraction(Long videoID, VideoInteractiveType interactiveType, Long num) {
        videoMapper.updateInteractiveNum(videoID, interactiveType.getDescription(), num);
    }

    @Override
    public String getPath(Long videoID) {
        Video video = getVideo(videoID);
        if (video == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_VIDEO);
        }
        return video.getVideoPath();
    }

    private Video getVideo(Long videoID) {
        Video video = new Video();
        video.setId(videoID);
        video.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        return videoMapper.selectOne(video);
    }
}
