package com.niuktok.backend.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niuktok.backend.common.def.ResponseStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel(description = "基础响应")
@NoArgsConstructor
public class GenericResponseVO<T> {
    @ApiModelProperty("状态码")
    private Integer status;

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("响应消息")
    private String message;

    @ApiModelProperty("响应时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public GenericResponseVO(ResponseStatusType status, T data) {
        this.timestamp = new Date();
        this.status = status.getCode();
        this.data = data;
        this.message = status.getDescription();
    }

    public static <T> GenericResponseVO<T> ok(T data, String msg) {
        GenericResponseVO<T> ans = new GenericResponseVO<>();
        ans.setData(data);
        ans.setMessage(msg);
        ans.setStatus(ResponseStatusType.SUCCESS.getCode());
        ans.setTimestamp(new Date());
        return ans;
    }

    public static <T> GenericResponseVO<T> ok(String msg) {
        return GenericResponseVO.ok(null, msg);
    }

    public static <T> GenericResponseVO<T> ok(T data) {
        return GenericResponseVO.ok(data, ResponseStatusType.SUCCESS.getDescription());
    }

    public static <T> GenericResponseVO<T> ok() {
        return GenericResponseVO.ok(ResponseStatusType.SUCCESS.getDescription());
    }

    public static <T> GenericResponseVO<T> error(T data, String msg) {
        GenericResponseVO<T> ans = new GenericResponseVO<>();
        ans.setData(data);
        ans.setMessage(msg);
        ans.setStatus(ResponseStatusType.ERROR.getCode());
        ans.setTimestamp(new Date());
        return ans;
    }

    public static <T> GenericResponseVO<T> error(String msg) {
        return GenericResponseVO.error(null, msg);
    }

    public static <T> GenericResponseVO<T> error(T data) {
        return GenericResponseVO.error(data, ResponseStatusType.ERROR.getDescription());
    }

    public static <T> GenericResponseVO<T> error() {
        return GenericResponseVO.error(ResponseStatusType.ERROR.getDescription());
    }
}
