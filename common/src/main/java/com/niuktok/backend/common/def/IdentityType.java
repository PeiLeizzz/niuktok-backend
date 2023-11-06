package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;
import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 登陆标识类型枚举类
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public enum IdentityType implements BaseEnum<Byte> {
    USERNAME((byte) 0, "username"),
    PHONE((byte) 1, "phone"),
    EMAIL((byte) 2, "email");

    @JsonValue
    private final Byte code;
    private final String description;

    IdentityType(Byte code, String msg) {
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

    public static IdentityType getByCode(Byte code) {
        for (IdentityType identityType : IdentityType.values()) {
            if (identityType.getCode().equals(code)) {
                return identityType;
            }
        }
        return null;
    }
}
