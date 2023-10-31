package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 登陆标识类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum IdentifierType implements BaseEnum<Integer> {
    USERNAME(0, "username"),
    PHONE(1, "phone"),
    EMAIL(2, "email");

    @JsonValue
    private final Integer code;
    private final String description;

    IdentifierType(Integer code, String msg) {
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
