package com.niuktok.backend.common.def;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private static final Map<Byte, IdentityType> indentityTypeMap = new HashMap<>();

    static {
        for (IdentityType identityType : IdentityType.values()) {
            indentityTypeMap.put(identityType.getCode(), identityType);
        }
    }

    public static IdentityType getByCode(Byte code) {
        return indentityTypeMap.get(code);
    }
}
