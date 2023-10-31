package com.niuktok.backend.common.def;

import com.niuktok.backend.common.annotation.SwaggerDisplayEnum;

/**
 * 通用枚举类型接口，用于反序列化
 * @param <T>
 */
@SwaggerDisplayEnum(index = "code", name = "description")
public interface BaseEnum<T> {
    /**
     * 每个枚举对象都有自己的 Code
     * 用于比较前端传递的 Code，将其转换成具体的枚举对象
     * @return Code
     */
    T getCode();

    String getDescription();
}
