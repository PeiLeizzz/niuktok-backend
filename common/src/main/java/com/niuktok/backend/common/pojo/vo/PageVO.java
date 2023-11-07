package com.niuktok.backend.common.pojo.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页查询基类")
public class PageVO<T> {
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;

    @ApiModelProperty(value = "当前页中条目数量")
    private Integer pageSize;

    @ApiModelProperty(value = "条目总数")
    private Long totalCount;

    private List<T> list;
}