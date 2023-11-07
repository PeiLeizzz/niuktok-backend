package com.niuktok.backend.common.def;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 交互类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum VideoInteractiveType implements BaseEnum<Byte> {
    LIKE((byte) 0, "like"),
    SHARE((byte) 1, "share"),
    FAVORITE((byte) 2, "favorite"),
    VIEW((byte) 3, "view");

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

    @JsonIgnore
    private static final Map<Byte, VideoInteractiveType> interactiveTypeMap = new HashMap<>();

    static {
        for (VideoInteractiveType interactiveType : VideoInteractiveType.values()) {
            interactiveTypeMap.put(interactiveType.getCode(), interactiveType);
        }
    }

    public static VideoInteractiveType getByCode(Byte code) {
        return interactiveTypeMap.get(code);
    }
}
