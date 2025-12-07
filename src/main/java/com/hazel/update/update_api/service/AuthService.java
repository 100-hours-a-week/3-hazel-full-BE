package com.hazel.update.update_api.service;

import com.hazel.update.update_api.common.exception.ApiException;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.config.jwt.JwtTokenProvider;
import com.hazel.update.update_api.dto.auth.*;
import com.hazel.update.update_api.entity.RefreshToken;
import com.hazel.update.update_api.entity.User;
import com.hazel.update.update_api.repository.RefreshTokenRepository;
import com.hazel.update.update_api.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        if(!isEmailAvailable(request.getEmail()))
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        if(!isNicknameAvailable(request.getNickname()))
            throw new ApiException(ErrorCode.DUPLICATED_NICKNAME);
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname());
        userRepository.save(user);
    }

    @Transactional
    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ApiException(ErrorCode.LOGIN_FAILED);

        String accessToken = jwtTokenProvider.generateAccessToken(user);

        refreshTokenRepository.deleteByUser(user);
        String refreshTokenValue = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(14);
        RefreshToken refreshToken = new RefreshToken(user, refreshTokenValue, expiresAt);
        refreshTokenRepository.save(refreshToken);

        LoginResponse loginResponse = LoginResponse.from(user, accessToken);

        return new LoginResult(loginResponse, refreshTokenValue);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.LOGIN_FAILED));
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public TokenRefreshResponse refreshAccessToken(String refreshTokenValue) {

        if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.isRevoked() || refreshToken.isExpired(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(refreshToken.getUser());

        return new TokenRefreshResponse(newAccessToken);
    }




}
