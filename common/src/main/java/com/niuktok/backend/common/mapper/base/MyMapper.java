package com.niuktok.backend.common.mapper.base;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@RegisterMapper
public interface MyMapper<T> extends Mapper<T> {

    /**
     * 保存一个实体，null 的属性不会保存，会使用数据库默认值
     *
     * @param records
     * @return
     */
    @InsertProvider(type = MyMapperProvider.class, method = "dynamicSQL")
    int insertListSelective(List<? extends T> records);
}