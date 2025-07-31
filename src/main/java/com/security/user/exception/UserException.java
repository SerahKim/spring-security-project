package com.security.user.exception;

import com.security.common.exception.BusinessException;
import com.security.common.exception.ErrorCode;
import org.apache.catalina.User;


public class UserException extends BusinessException {

    protected UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static UserException userNotFoundException() {
        return new UserException(UserErrorCode.USER_NOT_FOUND);
    }

    public static UserException pwdNotFoundException() {
        return new UserException(UserErrorCode.PWD_NOT_FOUND);
    }

    public static UserException duplicateEmailException() {
        return new UserException(UserErrorCode.EMAIL_DUPLICATED);
    }

    public static UserException tokenNotFoundException() {
        return new UserException(UserErrorCode.TOKEN_NOT_FOUND);
    }

    public static UserException expiredTokenException() {
        return new UserException(UserErrorCode.TOKEN_EXPIRED);
    }



}
