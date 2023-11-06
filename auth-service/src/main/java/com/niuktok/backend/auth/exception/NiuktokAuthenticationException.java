package com.niuktok.backend.auth.exception;

import org.springframework.security.core.AuthenticationException;

import com.niuktok.backend.common.def.ResponseStatusType;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class NiuktokAuthenticationException extends AuthenticationException {
    private ResponseStatusType responseStatusType;

    public NiuktokAuthenticationException(ResponseStatusType responseStatusType) {
        super(responseStatusType.getDescription());
        this.responseStatusType = responseStatusType;
    }

    public NiuktokAuthenticationException(ResponseStatusType responseStatusType, Throwable e) {
        super(responseStatusType.getDescription(), e);
        this.responseStatusType = responseStatusType;
    }

    public NiuktokAuthenticationException(ResponseStatusType responseStatusType, String message) {
        super(message);
        this.responseStatusType = responseStatusType;
    }

    public NiuktokAuthenticationException(ResponseStatusType responseStatusType, String message, Throwable e) {
        super(message, e);
        this.responseStatusType = responseStatusType;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
