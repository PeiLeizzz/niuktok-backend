package com.niuktok.backend.video.service;

import java.util.List;

import com.qiniu.storage.model.FileInfo;

public interface UploadService {
    String getQiniuToken();

    void uploadVideo(Long userID, FileInfo fileInfo, String videoKey, String title, String description, Long videoPartitionID, List<String> tags);

    FileInfo getFileInfo(String videoKey);
}
