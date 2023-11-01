package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;

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

    // 业务状态
    // 用户模块
    EXISTED_USER(10000, "user is already existed"),
    NOT_EXISTED_USER(10001, "user not existed");

    @JsonValue
    private final Integer code;
    private final String description;

    ResponseStatusType(Integer code, String msg) {
        this.code = code;
        this.description = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
