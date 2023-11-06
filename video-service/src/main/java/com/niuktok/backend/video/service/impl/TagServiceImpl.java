package com.niuktok.backend.video.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.entity.VideoTag;
import com.niuktok.backend.common.entity.VideoTagLink;
import com.niuktok.backend.common.mapper.VideoTagLinkMapper;
import com.niuktok.backend.common.mapper.VideoTagMapper;
import com.niuktok.backend.video.service.TagService;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private VideoTagMapper videoTagMapper;

    @Autowired
    private VideoTagLinkMapper videoTagLinkMapper;

    @Override
    @Transactional
    public List<Long> getOrInsertTags(List<String> tags) {
        List<Long> tagIDs = new ArrayList<>();
        for (String title : tags) {
            VideoTag videoTag = new VideoTag();
            videoTag.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
            videoTag.setTitle(title);
            VideoTag videoTagInDB = videoTagMapper.selectOne(videoTag);
            if (videoTagInDB == null) {
                videoTagMapper.insertSelective(videoTag);
                tagIDs.add(videoTag.getId());
            } else {
                tagIDs.add(videoTagInDB.getId());
            }
        }
        return tagIDs;
    }

    @Override
    @Transactional
    public void linkTagsForVideo(Long videoID, List<Long> tagIDs) {
        List<VideoTagLink> videoTagLinks = new ArrayList<>();
        for (Long tagID : tagIDs) {
            VideoTagLink videoTagLink = new VideoTagLink();
            videoTagLink.setVideoId(videoID);
            videoTagLink.setTagId(tagID);
            videoTagLinks.add(videoTagLink);
        }
        videoTagLinkMapper.insertListSelective(videoTagLinks);
    }
}
