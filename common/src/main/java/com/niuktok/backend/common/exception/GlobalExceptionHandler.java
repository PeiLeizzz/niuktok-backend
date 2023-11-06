package com.niuktok.backend.common.exception;

import cn.hutool.core.util.StrUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

@RestControllerAdvice(basePackages = "com.niuktok.backend")
@ConditionalOnMissingClass("com.niuktok.backend.gateway.GatewayApplication")
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理 JSON 请求体调用接口对象参数校验失败抛出的异常（RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseVO jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s: %s\n", fieldError.getField(), fieldError.getDefaultMessage());
            sb.append(msg);
        }
        return new BaseResponseVO(ResponseStatusType.INVALID_PARAMS, sb.toString());
    }


    /**
     * 处理单个参数校验失败抛出的异常（RequestParam / PathVariable）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponseVO ParamsException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            StringBuilder message = new StringBuilder();
            Path path = violation.getPropertyPath();
            String[] pathArr = StrUtil.splitToArray(path.toString(), '.');
            String msg = message.append(pathArr[1]).append(violation.getMessage()).toString();
            sb.append(msg);
            sb.append('\n');
        }
        return new BaseResponseVO(ResponseStatusType.INVALID_PARAMS, sb.toString());
    }

    /**
     * 处理 form-data 方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResponseVO formDaraParamsException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(o -> sb.append(String.format("%s: %s\n", o.getField(), o.getDefaultMessage())));
        return new BaseResponseVO(ResponseStatusType.INVALID_PARAMS, sb.toString());
    }

    /**
     * 请求方法不被允许异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponseVO httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new BaseResponseVO(ResponseStatusType.METHOD_NOT_ALLOWED);
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponseVO UnknownException(Exception e) {
        log.error("发生未知异常, msg: {}, cause: {}", e.getMessage(), e.getCause());
        e.printStackTrace();
        return new BaseResponseVO(ResponseStatusType.INTERNAL_SERVER_ERROR);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(NiuktokException.class)
    public BaseResponseVO BusinessException(NiuktokException e) {
        log.error("发生业务异常, msg: {}, cause: {}", e.getMessage(), e.getCause());
        e.printStackTrace();
        return new BaseResponseVO(e.getResponseStatusType(), e.getMessage());
    }
}
