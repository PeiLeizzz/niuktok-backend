package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 登陆标识类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum IdentifierType implements BaseEnum<Byte> {
    USERNAME((byte) 0, "username"),
    PHONE((byte) 1, "phone"),
    EMAIL((byte) 2, "email");

    @JsonValue
    private final Byte code;
    private final String description;

    IdentifierType(Byte code, String msg) {
        this.code = code;
        this.description = msg;
    }

    public Byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IdentifierType getByCode(Byte code) {
        for (IdentifierType identifierType : IdentifierType.values()) {
            if (identifierType.getCode().equals(code)) {
                return identifierType;
            }
        }
        return null;
    }
}
