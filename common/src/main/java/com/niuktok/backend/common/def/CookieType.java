package com.niuktok.backend.common.def;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CookieType implements BaseEnum<Byte> {
    USER_ID((byte) 0, "userID"),
    SESSION_ID((byte) 1, "sessionID");

    @JsonValue
    private final Byte code;
    private final String description;

    CookieType(Byte code, String msg) {
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
