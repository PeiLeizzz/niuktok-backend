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
@Table(name = "user_auth")
public class UserAuth implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "identity_type")
    private Byte identityType;

    private String identifier;

    private String credential;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

    private static final long serialVersionUID = 1L;
}