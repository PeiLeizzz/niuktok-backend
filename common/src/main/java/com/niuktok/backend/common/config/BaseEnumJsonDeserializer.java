package com.niuktok.backend.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.niuktok.backend.common.def.BaseEnum;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;

@Component
public class BaseEnumJsonDeserializer extends JsonDeserializer {
    @Override
    public BaseEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        BaseEnum result = null;
        try {
            // 获取当前待反序列化的字段名称
            String currentName = jsonParser.getCurrentName();
            // 通过反射获取当前字段
            Field declaredField = jsonParser.getCurrentValue().getClass().getDeclaredField(currentName);
            // 当对象类型是 BaseEnum 类型的子类
            if (BaseEnum.class.isAssignableFrom(declaredField.getType())) {
                // 枚举值
                Object[] enumConstants = declaredField.getType().getEnumConstants();
                for (Object obj : enumConstants) {
                    if (String.valueOf(((BaseEnum) obj).getCode()).equals(jsonParser.getText())) {
                        result = (BaseEnum) obj;
                        break;
                    }
                }
            }
        } catch (NoSuchFieldException e) {
        }
        return result;
    }
}