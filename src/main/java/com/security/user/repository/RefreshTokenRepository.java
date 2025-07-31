package com.security.user.repository;

import com.security.user.entity.RefreshTokenEntity;
import com.security.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUserEntity_UserId(Long userId);

}
