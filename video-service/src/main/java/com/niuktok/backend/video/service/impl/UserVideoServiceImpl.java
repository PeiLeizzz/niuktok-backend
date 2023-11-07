package com.niuktok.backend.video.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.video.service.UserVideoService;

@Service
public class UserVideoServiceImpl implements UserVideoService {
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public PageInfo<Video> getRelateVideos(VideoInteractiveType interactiveType, Long userID, Integer pageNo, Integer pageSize, String orderDir) {
        Page<Video> page = PageHelper.startPage(pageNo, pageSize);
        videoMapper.selectRelateVideos(userID, interactiveType.getDescription(), orderDir);
        return new PageInfo<>(page.getResult());
    }

    @Override
    public PageInfo<Video> getVideosOfUser(Long userID, Integer pageNo, Integer pageSize, String orderDir) {
        Page<Video> page = PageHelper.startPage(pageNo, pageSize);
        videoMapper.selectVideosOfUser(userID, orderDir);
        return new PageInfo<>(page.getResult());
    }
}
