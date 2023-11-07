package com.niuktok.backend.video.service;

import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.Video;

public interface UserVideoService {
    PageInfo<Video> getRelateVideos(VideoInteractiveType interactiveType, Long userID, Integer pageNo, Integer pageSize, String orderDir);

    PageInfo<Video> getVideosOfUser(Long userID, Integer pageNo, Integer pageSize, String orderDir);
}
