package com.niuktok.backend.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.niuktok.backend.common.validator.EnumConstraintValidator;


import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举类型检查
 */
@Documented
@Constraint(validatedBy = EnumConstraintValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(EnumCheck.List.class)
public @interface EnumCheck {
    /**
     * 提示信息
     *
     */
    String message() default "{javax.validation.constraints.EnumCheck.message}";

    /**
     * 分组
     *
     */
    Class<?>[] groups() default { };

    /**
     * 扩展对象
     *
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * 调用的方法名称
     */
    String method() default "getCode";

    /**
     * 要检查的枚举类型
     * @return
     */
    Class<?> clazz();

    /**
     * Defines several {@code @In} constraints on the same element.
     *
     * @see EnumCheck
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        /**
         * In 数组
         */
        EnumCheck[] value();
    }
}
