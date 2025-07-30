package com.security.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 비밀번호 유효성 검사를 위한 어노테이션
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "비밀번호 형식이 올바르지 않습니다.";

    int minLength() default 8;

    boolean containsSpecial() default true;

    boolean containsUppercase() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
