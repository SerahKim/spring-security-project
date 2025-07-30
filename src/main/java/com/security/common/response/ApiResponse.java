package com.security.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    // 성공 응답 (data 포함)
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus.value(), message, data);
    }

    // 성공 응답 (data 없음)
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.value(), message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }

}
