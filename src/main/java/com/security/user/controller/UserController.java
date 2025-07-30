package com.security.user.controller;

import com.security.common.response.ApiResponse;
import com.security.user.dto.SignupReqDTO;
import com.security.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupReqDTO signupReqDTO) {

        userService.signup(signupReqDTO);

        ApiResponse<Void> response = ApiResponse.success(HttpStatus.CREATED, "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
