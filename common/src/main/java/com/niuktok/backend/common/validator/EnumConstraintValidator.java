package com.niuktok.backend.common.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.niuktok.backend.common.annotation.EnumCheck;

public class EnumConstraintValidator implements ConstraintValidator<EnumCheck, Object> {
    /**
     * 注解对象
     */
    private EnumCheck annotation;

    /**
     * 初始化方法
     *
     * @param constraintAnnotation 注解对象
     */
    @Override
    public void initialize(EnumCheck constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        Object[] enumConstants = annotation.clazz().getEnumConstants();
        try {
            Method method = annotation.clazz().getMethod(annotation.method());
            for (Object enumConstant : enumConstants) {
                if (value.equals(method.invoke(enumConstant))) {
                    return true;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}