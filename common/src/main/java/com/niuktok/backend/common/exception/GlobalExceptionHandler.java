package com.niuktok.backend.common.exception;

import cn.hutool.core.util.StrUtil;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理 JSON 请求体调用接口对象参数校验失败抛出的异常（RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponseVO<List<String>> jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMsgList = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
            errorMsgList.add(msg);
        }
        return new GenericResponseVO<>(ResponseStatusType.INVALID_PARAMS, errorMsgList);
    }


    /**
     * 处理单个参数校验失败抛出的异常（RequestParam / PathVariable）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public GenericResponseVO<List<String>> ParamsException(ConstraintViolationException e) {
        List<String> errorMsgList = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            StringBuilder message = new StringBuilder();
            Path path = violation.getPropertyPath();
            String[] pathArr = StrUtil.splitToArray(path.toString(), '.');
            String msg = message.append(pathArr[1]).append(violation.getMessage()).toString();
            errorMsgList.add(msg);
        }
        return new GenericResponseVO<>(ResponseStatusType.INVALID_PARAMS, errorMsgList);
    }

    /**
     * 处理 form-data 方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public GenericResponseVO<List<String>> formDaraParamsException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errorMsgList = fieldErrors.stream()
                .map(o -> o.getField() + ": " + o.getDefaultMessage())
                .collect(Collectors.toList());
        return new GenericResponseVO<>(ResponseStatusType.INVALID_PARAMS, errorMsgList);
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
    public BaseResponseVO UnNoException(Exception e) {
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
        return new BaseResponseVO(e.getResponseStatusType());
    }
}
