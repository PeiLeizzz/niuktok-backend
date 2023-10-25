package com.niuktok.backend.common.entity;

import java.io.Serializable;
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
    private Integer id;

    private String username;

    private static final long serialVersionUID = 1L;
}