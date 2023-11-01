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
@Table(name = "video_tag")
public class VideoTag implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String title;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

    private static final long serialVersionUID = 1L;
}