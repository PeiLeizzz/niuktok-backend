package com.niuktok.backend.common.def;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 性别枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum SexType implements BaseEnum<Byte> {
    UNKNOWN((byte) 0, "unknown"),
    MAIL((byte) 1, "mail"),
    FEMAIL((byte) 2, "femail");

    @JsonValue
    private final Byte code;
    private final String description;

    SexType(Byte code, String msg) {
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
    private static final Map<Byte, SexType> sexTypeMap = new HashMap<>();

    static {
        for (SexType responseStatusType : SexType.values()) {
            sexTypeMap.put(responseStatusType.getCode(), responseStatusType);
        }
    }

    public static SexType getByCode(Byte code) {
        return sexTypeMap.get(code);
    }
}   
