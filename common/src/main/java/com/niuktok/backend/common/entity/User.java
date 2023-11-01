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
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String username;

    private String avatar;

    private Byte sex;

    @Column(name = "followed_num")
    private Long followedNum;

    @Column(name = "follower_num")
    private Long followerNum;

    private Byte status;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

    private static final long serialVersionUID = 1L;
}