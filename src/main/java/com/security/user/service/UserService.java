package com.security.user.service;

import com.security.user.dto.SignupReqDTO;
import com.security.user.entity.UserEntity;
import com.security.user.mapper.UserMapper;
import com.security.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.security.user.exception.UserException.duplicateEmail;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
            throw duplicateEmail();
        }

        /* UserEntity 저장 */
        // 비밀번호 해시화
        String hashedPwd = passwordEncoder.encode(signupReqDTO.getPassword());

        // SignupReqDTO -> UserEntity 변환
        UserEntity userEntity = userMapper.toUserEntity(signupReqDTO, hashedPwd);

        // UserEntity 저장
        userRepository.save(userEntity);
    }


}
