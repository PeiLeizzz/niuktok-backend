package com.niuktok.backend.video.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.video.config.QiniuConfigurer;
import com.niuktok.backend.video.service.VideoService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Json;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private QiniuConfigurer qiniuConfigurer;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private WFGIdGenerator wfgIdGenerator;

    @Override
    public String getQiniuToken() {
        return qiniuConfigurer.qiniuAuth().uploadToken(
            qiniuConfigurer.getBucket(), 
            null, 
            qiniuConfigurer.getExpireTimes(),
            qiniuConfigurer.putPolicy()
        );
    }

    @Override
    public void uploadVideo(Long userID, String videoKey, String description) {
        try {
			FileInfo fileInfo = qiniuConfigurer.bucketManager().stat(qiniuConfigurer.getBucket(), videoKey);
            Video video = new Video();
            video.setId(wfgIdGenerator.next());
            video.setUserId(userID);
            video.setDescription(description);
            video.setVideoPath(videoKey);
            video.setInfo(Json.encode(fileInfo));
            videoMapper.insertSelective(video);
		} catch (QiniuException e) {
            if (e.getMessage() != null && e.getMessage().contains("no such file or directory")) {
                throw new NiuktokException(ResponseStatusType.INVALID_VIDEO_KEY);
            }
			throw new RuntimeException(e);
		}
    }
}
