package com.security.user.exception;

import com.security.common.exception.BusinessException;
import com.security.common.exception.ErrorCode;


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

}
