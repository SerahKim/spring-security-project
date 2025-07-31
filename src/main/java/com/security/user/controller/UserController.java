package com.security.user.controller;

import com.security.common.response.ApiResponse;
import com.security.user.dto.LoginReqDTO;
import com.security.user.dto.LoginResDTO;
import com.security.user.dto.SignupReqDTO;
import com.security.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupReqDTO signupReqDTO) {

        userService.signup(signupReqDTO);

        ApiResponse<Void> response = ApiResponse.success(HttpStatus.CREATED, "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDTO>> login(@Valid @RequestBody LoginReqDTO loginReqDTO, HttpServletResponse response) {

        LoginResDTO loginResDTO = userService.login(loginReqDTO);

        // Refresh Token 쿠키 저장
        String refreshToken = loginResDTO.getRefreshToken();
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // 개발 단계에서는 false
                .sameSite("Lax")
                .path("/")
                .maxAge(refreshTokenValidityInSeconds)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        ApiResponse<LoginResDTO> apiResponse = ApiResponse.success(HttpStatus.OK, "로그인에 성공하였습니다.", loginResDTO);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Access Token 재발급
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<LoginResDTO>> reissueAccessToken(@CookieValue("refreshToken") String refreshToken) {

        LoginResDTO loginResDTO = userService.reissueAccessToken(refreshToken);

        ApiResponse<LoginResDTO> apiResponse = ApiResponse.success(HttpStatus.OK, "토큰이 재발급되었습니다.", loginResDTO);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
