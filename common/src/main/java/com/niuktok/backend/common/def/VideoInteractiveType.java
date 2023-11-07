package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 交互类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum VideoInteractiveType implements BaseEnum<Byte> {
    LIKE((byte) 0, "like_num"),
    SHARE((byte) 1, "share_num"),
    FAVORITE((byte) 2, "favorite_num"),
    VIEW((byte) 3, "view_num");

    @JsonValue
    private final Byte code;
    private final String description;

    VideoInteractiveType(Byte code, String msg) {
        this.code = code;
        this.description = msg;
    }

    @Override
    public Byte getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
