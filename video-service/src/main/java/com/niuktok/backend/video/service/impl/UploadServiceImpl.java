package com.niuktok.backend.video.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.video.config.QiniuConfigurer;
import com.niuktok.backend.video.service.PartitionService;
import com.niuktok.backend.video.service.TagService;
import com.niuktok.backend.video.service.UploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Json;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private QiniuConfigurer qiniuConfigurer;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private WFGIdGenerator wfgIdGenerator;

    @Autowired
    private TagService tagService;

    @Autowired
    private PartitionService partitionService;

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
    public FileInfo getFileInfo(String videoKey) {
        try {
            return qiniuConfigurer.bucketManager().stat(qiniuConfigurer.getBucket(), videoKey);
        } catch (QiniuException e) {
            if (e.getMessage() != null && e.getMessage().contains("no such file or directory")) {
                throw new NiuktokException(ResponseStatusType.INVALID_VIDEO_KEY);
            }
			throw new RuntimeException(e);
		}
    }

    @Override
    @Transactional
    public void uploadVideo(Long userID, FileInfo fileInfo, String videoKey, String title, String description, Long partitionID, List<String> tags) {
        if (!partitionService.exist(partitionID)) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_PARTITION);
        }

        Video video = new Video();
        video.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        video.setUserId(userID);
        video.setVideoKey(videoKey);
        if (videoMapper.selectCount(video) > 0) {
            throw new NiuktokException(ResponseStatusType.DUPLICATED_VIDEO);
        }

        video.setId(wfgIdGenerator.next());
        video.setTitle(title);
        video.setDescription(description);
        video.setPartitionId(partitionID);
        // path 暂时也用 key
        video.setVideoPath(videoKey);
        video.setInfo(Json.encode(fileInfo));
        videoMapper.insertSelective(video);

        if (tags.size() > 0) {
            List<Long> tagIDs = tagService.getOrInsertTags(tags);
            tagService.linkTagsForVideo(video.getId(), tagIDs);
        }
    }
}
