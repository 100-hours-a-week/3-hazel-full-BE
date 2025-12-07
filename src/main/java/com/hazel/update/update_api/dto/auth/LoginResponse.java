package com.hazel.update.update_api.dto.auth;

import com.hazel.update.update_api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String email;
    private String nickname;
    private String accessToken;

    public static LoginResponse from(User user,String accessToken) {
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                accessToken
        );
    }
}
