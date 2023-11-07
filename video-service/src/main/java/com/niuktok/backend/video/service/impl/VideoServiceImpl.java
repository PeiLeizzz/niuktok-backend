package com.niuktok.backend.video.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.video.config.QiniuConfigurer;
import com.niuktok.backend.video.service.VideoService;
import com.qiniu.common.QiniuException;

@Service
public class VideoServiceImpl implements VideoService {
    // 最近的阈值界定
    private static final long RANDOM_RANGE = 10000L;

    // 最多的查询次数（防止视频不够无限循环）
    private static final int MAX_SELECT_CNT = 10; 

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private QiniuConfigurer qiniuConfigurer;

    @Override
    public boolean exist(Long videoID) {
        Video video = new Video();
        video.setId(videoID);
        video.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        return videoMapper.selectCount(video) == 1;
    }

    @Override
    public void syncInteraction(Long videoID, Byte interactiveType, Long num) {
        videoMapper.updateInteractiveNum(videoID, VideoInteractiveType.getByCode(interactiveType).getDescription(), num);
    }

    @Override
    public String getPath(Long videoID) {
        Video video = getVideo(videoID);
        if (video == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_VIDEO);
        }
        return concatPrivatePath(video.getVideoPath());
    }

    private Video getVideo(Long videoID) {
        Video video = new Video();
        video.setId(videoID);
        video.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        return videoMapper.selectOne(video);
    }

    @Override
    public List<Video> anonymousPull(Integer size) {
        // 未登录用户从所有视频中随机抽取 size 个即可
        return videoMapper.selectRandom(size, new ArrayList<>());
    }

    @Override
    public List<Video> userPull(Long userID, Integer size) {
        // NOTE: 当前为简化策略
        // 登录用户按照时间倒序从前 RANDOM_RANGE 条没有看过的视频随机挑选 size 条
        // 并且不拉取自己发布的视频
        // 如果数量不够，再按照未登录用户的随机策略补全剩下的数量
        List<Video> videos = videoMapper.selectUserRecentRandom(userID, RANDOM_RANGE, size);
        int selectCnt = 0;
        while (videos.size() < size && selectCnt < MAX_SELECT_CNT) {
            videos.addAll(videoMapper.selectRandom(size - videos.size(), videos));
            selectCnt++;
        }
        return videos;
    }

    @Override
    public String concatPrivatePath(String key) {
        try {
            return qiniuConfigurer.getPrivatePath(key);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}
