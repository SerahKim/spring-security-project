package com.security.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDTO {

    private String accessToken;
    private String refreshToken;

}
