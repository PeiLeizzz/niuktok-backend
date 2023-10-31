package com.niuktok.backend.common.exception;

import com.niuktok.backend.common.def.ResponseStatusType;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class NiuktokException extends RuntimeException {
    private ResponseStatusType responseStatusType;

    public NiuktokException(ResponseStatusType responseStatusType) {
        super(responseStatusType.getDescription());
        this.responseStatusType = responseStatusType;
    }

    public NiuktokException(ResponseStatusType responseStatusType, Throwable e) {
        super(responseStatusType.getDescription(), e);
        this.responseStatusType = responseStatusType;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
