package com.hazel.update.update_api.repository;

import com.hazel.update.update_api.entity.RefreshToken;
import com.hazel.update.update_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
