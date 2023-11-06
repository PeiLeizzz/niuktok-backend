package com.niuktok.backend.common.def;

public enum LogicDeleteEnum {
    DELETED(true),
    NOT_DELETED(false);

    private boolean value;

    LogicDeleteEnum(boolean status) {
        this.value = status;
    }

    public boolean value() {
        return this.value;
    }
}
