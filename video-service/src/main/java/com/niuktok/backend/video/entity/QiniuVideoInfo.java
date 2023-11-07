package com.niuktok.backend.video.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QiniuVideoInfo {
    private String key;

    private String md5;

    private String hash;

    private Integer type;

    private Long fsize;

    private Integer status;

    private String endUser;

    private Long putTime;

    private String mimeType;

    @JSONField(name = "x-qn-meta")
    private String xQnMeta;

    private Long expiration;

    private Integer restoreStatus;

    private Long transitionToIA;

    private Long transitionToARCHIVE;

    private Long transitionToDeepArchive;
}
