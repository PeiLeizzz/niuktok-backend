package com.niuktok.backend.interactive.service;

public interface InteractionSync {
    void syncViewNum(Long videoID, Long num);

    void syncLikeNum(Long videoID, Long num);

    void syncFavoriteNum(Long videoID, Long num);

    void syncShareNum(Long videoI, Long numD);
}