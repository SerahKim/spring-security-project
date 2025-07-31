package com.security.user.dto;

import com.security.util.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginReqDTO {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;

    @Password
    private String password;
}
