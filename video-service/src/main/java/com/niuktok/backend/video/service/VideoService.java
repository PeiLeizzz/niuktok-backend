package com.niuktok.backend.video.service;

public interface VideoService {
    boolean exist(Long videoID);
    
    void syncInteraction(Long videoID, Byte interactiveType, Long num);

    String getPath(Long videoID);
}
