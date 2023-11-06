package com.niuktok.backend.video.service;

import java.util.List;

public interface TagService {
    List<Long> getOrInsertTags(List<String> tags);

    void linkTagsForVideo(Long videoTag, List<Long> tagIDs);
}
