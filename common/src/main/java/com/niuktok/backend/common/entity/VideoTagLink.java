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
@Table(name = "video_tag_link")
public class VideoTagLink implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    private static final long serialVersionUID = 1L;
}