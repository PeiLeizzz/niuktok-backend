package com.niuktok.backend.interactive.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.UserVideoFavorite;
import com.niuktok.backend.common.entity.UserVideoLike;
import com.niuktok.backend.common.entity.UserVideoShare;
import com.niuktok.backend.common.entity.UserVideoView;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserVideoFavoriteMapper;
import com.niuktok.backend.common.mapper.UserVideoLikeMapper;
import com.niuktok.backend.common.mapper.UserVideoShareMapper;
import com.niuktok.backend.common.mapper.UserVideoViewMapper;
import com.niuktok.backend.interactive.service.InteractionSync;
import com.niuktok.backend.interactive.service.InteractiveService;
import com.niuktok.backend.interactive.service.feign.VideoFeign;

@Service
public class InteractiveServiceImpl implements InteractiveService {
    @Autowired
    private VideoFeign videoFeign;

    @Autowired
    private InteractionSync interactionSync;

    @Autowired
    private UserVideoViewMapper viewMapper;

    @Autowired
    private UserVideoLikeMapper likeMapper;

    @Autowired
    private UserVideoShareMapper shareMapper;

    @Autowired
    private UserVideoFavoriteMapper favoriteMapper;

    @Override
    @Transactional
    public void view(Long userID, long videoID, Integer progress) {
        UserVideoView view = new UserVideoView();
        view.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        view.setUserId(userID);
        view.setVideoId(videoID);
        
        UserVideoView viewInDB = viewMapper.selectOne(view);
        if (viewInDB == null) {
            view.setCount(1);
            view.setLastProgress(progress);
            viewMapper.insertSelective(view);
        } else {
            viewInDB.setCount(viewInDB.getCount() + 1);
            viewInDB.setLastProgress(progress);
            viewMapper.updateByPrimaryKeySelective(viewInDB);
        }

        interactionSync.syncViewNum(videoID, 1L);
    }

    @Override
    @Transactional
    public void like(Long userID, Long videoID) {
        UserVideoLike like = new UserVideoLike();
        like.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        like.setUserId(userID);
        like.setVideoId(videoID);

        UserVideoLike likeInDB = likeMapper.selectOne(like);
        if (likeInDB != null) {
            throw new NiuktokException(ResponseStatusType.DUPLICATED_LIKED); 
        } 
        likeMapper.insertSelective(like);

        interactionSync.syncLikeNum(videoID, 1L);
    }

    @Override
    @Transactional
    public void cancelLike(Long userID, long videoID) {
        UserVideoLike like = new UserVideoLike();
        like.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        like.setUserId(userID);
        like.setVideoId(videoID);

        UserVideoLike likeInDB = likeMapper.selectOne(like);
        if (likeInDB == null) {
            throw new NiuktokException(ResponseStatusType.NOT_LIKED);
        }
        likeInDB.setIsDeleted(LogicDeleteEnum.DELETED.value());
        likeInDB.setDeletedTime(new Date());
        likeMapper.updateByPrimaryKeySelective(likeInDB);

        interactionSync.syncLikeNum(videoID, -1L);
    }

    @Override
    @Transactional
    public void favorite(Long userID, long videoID) {
        UserVideoFavorite favorite = new UserVideoFavorite();
        favorite.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        favorite.setUserId(userID);
        favorite.setVideoId(videoID);

        UserVideoFavorite favoriteInDB = favoriteMapper.selectOne(favorite);
        if (favoriteInDB != null) {
            throw new NiuktokException(ResponseStatusType.DUPLICATED_FAVORITED); 
        } 
        favoriteMapper.insertSelective(favorite);

        interactionSync.syncFavoriteNum(videoID, 1L);
    }

    @Override
    @Transactional
    public void cancelFavorite(Long userID, long videoID) {
        UserVideoFavorite favorite = new UserVideoFavorite();
        favorite.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        favorite.setUserId(userID);
        favorite.setVideoId(videoID);

        UserVideoFavorite favoriteInDB = favoriteMapper.selectOne(favorite);
        if (favoriteInDB == null) {
            throw new NiuktokException(ResponseStatusType.NOT_FAVORITED); 
        } 
        favoriteInDB.setIsDeleted(LogicDeleteEnum.DELETED.value());
        favoriteInDB.setDeletedTime(new Date());
        favoriteMapper.updateByPrimaryKeySelective(favoriteInDB);

        interactionSync.syncFavoriteNum(videoID, -1L);
    }

    @Override
    @Transactional
    public String share(Long userID, long videoID) {
        UserVideoShare share = new UserVideoShare();
        share.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        share.setUserId(userID);
        share.setVideoId(videoID);

        UserVideoShare shareInDB = shareMapper.selectOne(share);
        if (shareInDB == null) {
            share.setCount(1);
            shareMapper.insertSelective(share);
        } else {
            shareInDB.setCount(shareInDB.getCount() + 1);
            shareMapper.updateByPrimaryKey(shareInDB);
        }

        String url = videoFeign.getPath(userID).getData();
        interactionSync.syncShareNum(videoID, 1L);
        return url;
    }
}
