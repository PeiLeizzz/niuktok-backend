package com.niuktok.backend.common.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name = "video")
public class Video implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String title;

    private String description;

    @Column(name = "video_key")
    private String videoKey;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "view_num")
    private Long viewNum;

    @Column(name = "like_num")
    private Long likeNum;

    @Column(name = "favorite_num")
    private Long favoriteNum;

    @Column(name = "share_num")
    private Long shareNum;

    @Column(name = "partition_id")
    private Long partitionId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    private String info;

    private static final long serialVersionUID = 1L;
}