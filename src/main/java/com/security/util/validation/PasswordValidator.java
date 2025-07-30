package com.security.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int minLength;
    private boolean containsSpecial;
    private boolean containsUppercase;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.containsSpecial = constraintAnnotation.containsSpecial();
        this.containsUppercase = constraintAnnotation.containsUppercase();
    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext context) {

        /* 비밀번호 필수값 검사 */
        if (pwd == null || pwd.isEmpty()) {
            // 기본 메시지 비활성화
            context.disableDefaultConstraintViolation();
            // 커스텀 메시지 생성 및 활성화
            context.buildConstraintViolationWithTemplate("비밀번호는 필수 입력값입니다.").addConstraintViolation();
        }

        /* 비밀번호 패턴 검사 */
        // 비밀번호 최소 길이 검사(default = 8)
        if (pwd.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호는 최소 " + minLength + "자리 이상이어야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        // 특수문자 포함 검사(default = true)
        if (containsSpecial && !pwd.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호에 특수문자가 하나 이상 포함되어야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        // 대문자 포함 검사(default = false)
        if (containsUppercase && !pwd.matches(".*[A-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호에 대문자가 하나 이상 포함되어야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
