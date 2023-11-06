package com.niuktok.backend.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niuktok.backend.common.def.ResponseStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel(description = "基础响应，无数据")
@NoArgsConstructor
public class BaseResponseVO {
    @ApiModelProperty("状态码")
    private Integer status;

    @ApiModelProperty("数据，始终为 null")
    private final Object data = null;

    @ApiModelProperty("响应消息")
    private String message;

    @ApiModelProperty("响应时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public BaseResponseVO(ResponseStatusType status) {
        this.timestamp = new Date();
        this.status = status.getCode();
        this.message = status.getDescription();
    }

    public BaseResponseVO(ResponseStatusType status, String msg) {
        this.timestamp = new Date();
        this.status = status.getCode();
        this.message = msg;
    }

    public static BaseResponseVO ok(String msg) {
        BaseResponseVO ans = new BaseResponseVO();
        ans.setMessage(msg);
        ans.setStatus(ResponseStatusType.SUCCESS.getCode());
        ans.setTimestamp(new Date());
        return ans;
    }

    public static BaseResponseVO ok() {
        return BaseResponseVO.ok(ResponseStatusType.SUCCESS.getDescription());
    }

    public static BaseResponseVO error(String msg) {
        BaseResponseVO ans = new BaseResponseVO();
        ans.setMessage(msg);
        ans.setStatus(ResponseStatusType.ERROR.getCode());
        ans.setTimestamp(new Date());
        return ans;
    }

    public static BaseResponseVO error() {
        return BaseResponseVO.error(ResponseStatusType.ERROR.getDescription());
    }
}
