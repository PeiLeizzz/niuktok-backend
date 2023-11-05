package com.niuktok.backend.video.service;

public interface VideoService {
    String getQiniuToken();

    void uploadVideo(Long userID, String videoKey, String description);
}
