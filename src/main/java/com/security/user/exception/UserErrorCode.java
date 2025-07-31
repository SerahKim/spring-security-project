package com.security.user.exception;

import com.security.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

    // NOT_FOUND
    USER_NOT_FOUND(1001, "사용자를 찾을 없습니다.", HttpStatus.NOT_FOUND),
    PWD_NOT_FOUND(1003, "유효하지 않은 이메일입니다.", HttpStatus.NOT_FOUND),

    // CONFLICT
    EMAIL_DUPLICATED(1010, "이미 등록된 이메일입니다.", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    UserErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
