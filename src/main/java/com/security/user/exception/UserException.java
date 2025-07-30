package com.security.user.exception;

import com.security.common.exception.BusinessException;
import com.security.common.exception.ErrorCode;


public class UserException extends BusinessException {

    protected UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static UserException duplicateEmail() {
        return new UserException(UserErrorCode.EMAIL_DUPLICATED);
    }

}
