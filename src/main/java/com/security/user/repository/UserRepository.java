package com.security.user.repository;

import com.security.user.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    
    Optional<UserEntity> findByEmail(@NotBlank(message = "이메일은 필수입니다.") @Email String email);

    UserEntity findByUserId(Long userId);
}
