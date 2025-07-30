package com.security.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    int getCode(); // 비지니스 로직 구분용 에러 코드
    String getMessage(); // 사용자에게 보여줄 에러 메시지
    HttpStatus getHttpStatus(); // HTTP 상태 코드

}
