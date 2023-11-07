package com.niuktok.backend.video.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.def.VideoInteractiveType;
import com.niuktok.backend.common.entity.UserVideoFavorite;
import com.niuktok.backend.common.entity.UserVideoLike;
import com.niuktok.backend.common.entity.UserVideoShare;
import com.niuktok.backend.common.entity.UserVideoView;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.entity.VideoPartition;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserVideoFavoriteMapper;
import com.niuktok.backend.common.mapper.UserVideoLikeMapper;
import com.niuktok.backend.common.mapper.UserVideoShareMapper;
import com.niuktok.backend.common.mapper.UserVideoViewMapper;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.common.mapper.VideoPartitionMapper;
import com.niuktok.backend.video.config.QiniuConfigurer;
import com.niuktok.backend.video.pojo.vo.PartitionVO.PartitionInfoVO;
import com.niuktok.backend.video.pojo.vo.VideoDetailVO;
import com.niuktok.backend.video.pojo.vo.VideoDetailVO.InteractiveInfo;
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
    private VideoPartitionMapper partitionMapper;

    @Autowired
    private UserVideoViewMapper viewMapper;

    @Autowired
    private UserVideoLikeMapper likeMapper;

    @Autowired
    private UserVideoShareMapper shareMapper;

    @Autowired
    private UserVideoFavoriteMapper favoriteMapper;

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

    @Override
    @Transactional
    public VideoDetailVO detail(Long userID, Long videoID) {
        Video video = getVideo(videoID);
        if (video == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_VIDEO);
        }
        VideoDetailVO detail = new VideoDetailVO();

        // 分区信息
        VideoPartition partition = new VideoPartition();
        partition.setId(video.getPartitionId());
        partition.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        partition = partitionMapper.selectOne(partition);
        if (partition != null) {
            // 分区不存在时保持 null，不抛错
            detail.setPartitionInfo(new PartitionInfoVO(partition));
        }

        // 用户交互信息
        InteractiveInfo interactiveInfo = new InteractiveInfo();
        detail.setInteractiveInfo(interactiveInfo);
        if (userID == null) {
            return detail;
        }
        
        UserVideoView view = new UserVideoView();
        view.setUserId(userID);
        view.setVideoId(videoID);
        view.setIsDeleted((LogicDeleteEnum.NOT_DELETED.value()));
        view = viewMapper.selectOne(view);
        if (view != null) {
            interactiveInfo.setLastProgress(view.getLastProgress());
        }

        UserVideoLike like = new UserVideoLike();
        like.setUserId(userID);
        like.setVideoId(videoID);
        like.setIsDeleted((LogicDeleteEnum.NOT_DELETED.value()));
        like = likeMapper.selectOne(like);
        interactiveInfo.setIsLiked(likeMapper.selectCount(like) > 0);

        UserVideoShare share = new UserVideoShare();
        share.setUserId(userID);
        share.setVideoId(videoID);
        share.setIsDeleted((LogicDeleteEnum.NOT_DELETED.value()));
        share = shareMapper.selectOne(share);
        interactiveInfo.setIsShared(shareMapper.selectCount(share) > 0);

        UserVideoFavorite favorite = new UserVideoFavorite();
        favorite.setUserId(userID);
        favorite.setVideoId(videoID);
        favorite.setIsDeleted((LogicDeleteEnum.NOT_DELETED.value()));
        favorite = favoriteMapper.selectOne(favorite);
        interactiveInfo.setIsFavorited(favoriteMapper.selectCount(favorite) > 0);

        return detail;
    }
}
