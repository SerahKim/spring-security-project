package com.security.user.service;

import com.security.auth.jwt.JwtTokenProvider;
import com.security.user.dto.LoginReqDTO;
import com.security.user.dto.LoginResDTO;
import com.security.user.dto.SignupReqDTO;
import com.security.user.entity.RefreshTokenEntity;
import com.security.user.entity.UserEntity;
import com.security.user.exception.UserException;
import com.security.user.mapper.UserMapper;
import com.security.user.repository.RefreshTokenRepository;
import com.security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.security.user.exception.UserException.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 이메일 중복 확인
    @Transactional(readOnly = true)
    public boolean checkAvailEmail (String email) {
        // true: 이미 존재, false : 중복 아님
        boolean alreadyHasEmail = userRepository.existsByEmail(email);

        // true : 사용 가능, false : 중복
        return !alreadyHasEmail;
    }

    // 회원 가입
    @Transactional
    public void signup(SignupReqDTO signupReqDTO) {

        // 이메일 중복 확인
        if (!checkAvailEmail(signupReqDTO.getEmail())) {
            throw duplicateEmailException();
        }

        /* UserEntity 저장 */
        // 비밀번호 해시화
        String hashedPwd = passwordEncoder.encode(signupReqDTO.getPassword());

        // SignupReqDTO -> UserEntity 변환
        UserEntity userEntity = userMapper.toUserEntity(signupReqDTO, hashedPwd);

        // UserEntity 저장
        userRepository.save(userEntity);
    }

    // 로그인
    @Transactional
    public LoginResDTO login(LoginReqDTO loginReqDTO) {

        // 이메일로 사용자 조회
        UserEntity userEntity = userRepository.findByEmail(loginReqDTO.getEmail())
                .orElseThrow(UserException::userNotFoundException);

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginReqDTO.getPassword(), userEntity.getPassword())) {
            throw pwdNotFoundException();
        }

        // userId
        Long userId = userEntity.getUserId();

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // refresh token db 저장
        saveRefreshToken(userId, refreshToken);

        // LoginResDTO 반환
        return new LoginResDTO(accessToken, refreshToken);

    }

    // refresh token DB 저장
    private void saveRefreshToken(Long userId, String refreshToken) {

        // 해당 userId를 가진 UserEntity 조회
        UserEntity userEntity = userRepository.findByUserId(userId);

        // 기존 refresh token 있다면 삭제
        refreshTokenRepository.findByUserEntity_UserId(userId).ifPresent(refreshTokenRepository::delete);

        // refresh token DB 저장
        LocalDateTime issuedAt = LocalDateTime.ofInstant(jwtTokenProvider.getIssuedAt(refreshToken), ZoneId.systemDefault());
        LocalDateTime expiredAt = LocalDateTime.ofInstant(jwtTokenProvider.getExpiredAt(refreshToken), ZoneId.systemDefault());
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userEntity(userEntity)
                .refreshToken(refreshToken)
                .issuedAt(issuedAt)
                .expiredAt(expiredAt)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    // Access Token 재발급
    @Transactional
    public LoginResDTO reissueAccessToken(String refreshToken) {

        // 사용자로부터 받은 토큰 유효성 검사
        if (refreshToken == null || refreshToken.isBlank()) {
            throw tokenNotFoundException();
        }

        // DB 조회 및 유효성 검사
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(UserException::tokenNotFoundException);

        // refresh token 만료일 확인
        if (refreshTokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw expiredTokenException();
        }

        UserEntity userEntity = refreshTokenEntity.getUserEntity();
        if (userEntity == null) {
            throw userNotFoundException();
        }

        // access token 재발급
        String accessToken = jwtTokenProvider.generateAccessToken(userEntity.getUserId());

        // access token 반환
        return new LoginResDTO(accessToken, null);

    }

}
