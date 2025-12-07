package com.hazel.update.update_api.service;

import com.hazel.update.update_api.common.exception.ApiException;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.config.jwt.UserPrincipal;
import com.hazel.update.update_api.dto.challenge.MyChallengeResponse;
import com.hazel.update.update_api.dto.user.ChangePasswordRequest;
import com.hazel.update.update_api.dto.user.UserProfileResponse;
import com.hazel.update.update_api.entity.User;
import com.hazel.update.update_api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return UserProfileResponse.from(user);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void changeNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if (user.getNickname().equals(nickname))
            return;
        if(!isNicknameAvailable(nickname))
            throw new ApiException(ErrorCode.DUPLICATED_NICKNAME);
        user.changeNickname(nickname);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new ApiException(ErrorCode.INVALID_CURRENT_PASSWORD);
        if (request.getCurrentPassword().equals(request.getNewPassword()))
            throw new ApiException(ErrorCode.SAME_AS_CURRENT_PASSWORD);
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

}