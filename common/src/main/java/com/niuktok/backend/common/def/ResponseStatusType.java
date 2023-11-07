package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应状态枚举类
 */
public enum ResponseStatusType implements BaseEnum<Integer> {
    // 通用状态
    SUCCESS(0, "success"),
    ERROR(1, "error"),
    INVALID_PARAMS(2, "invalid params"),
    METHOD_NOT_ALLOWED(3, "method not allowed"),
    INTERNAL_SERVER_ERROR(4, "internal server error"),
    UNAUTHORIZED(5, "unauthorized"),
    WRONG_CREDENTIAL(6, "wrong credential"),
    NO_PERMISSIONS(7, "no permissions"),
    EXPIRED_TOKEN(8, "expired token"),
    WRONG_TOKEN(9, "wrong token"),

    // 业务状态
    // 用户模块
    EXISTED_USER(10000, "user is already existed"),
    NOT_EXISTED_USER(10001, "user not existed"),
    EXISTED_IDENTIFIER(10002, "identifier is already existed"),
    NOT_EXISTED_IDENTIFIER(10003, "identifier not existed"),

    // 视频模块
    DUPLICATED_VIDEO(20000, "video is already uploaded by this user"),
    INVALID_VIDEO_KEY(20001, "invalid video key"),
    NOT_EXISTED_PARTITION(20002, "partition not existed"),
    NOT_EXISTED_VIDEO(20003, "video not existed"),

    // 交互模块
    DUPLICATED_LIKED(30000, "current user has already liked this video before"),
    NOT_LIKED(30001, "current user not liked this video before"),
    DUPLICATED_FAVORITED(30002, "current user has already favorited this video before"),
    NOT_FAVORITED(30003, "current user not favorited this video before");

    @JsonValue
    private final Integer code;
    private final String description;

    ResponseStatusType(Integer code, String msg) {
        this.code = code;
        this.description = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    private static final Map<Integer, ResponseStatusType> responseStatusMap = new HashMap<>();

    static {
        for (ResponseStatusType responseStatusType : ResponseStatusType.values()) {
            responseStatusMap.put(responseStatusType.getCode(), responseStatusType);
        }
    }

    public static ResponseStatusType getByCode(Integer code) {
        return responseStatusMap.get(code);
    }
}
