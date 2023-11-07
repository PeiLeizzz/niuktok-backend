package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 排序类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum OrderType implements BaseEnum<Byte> {
    ASC((byte) 0, "asc"),
    DESC((byte) 1, "desc");

    @JsonValue
    private final Byte code;
    private final String description;

    OrderType(Byte code, String msg) {
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
