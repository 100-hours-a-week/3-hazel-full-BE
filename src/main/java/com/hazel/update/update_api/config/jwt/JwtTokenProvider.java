package com.hazel.update.update_api.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hazel.update.update_api.entity.User;
import com.hazel.update.update_api.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Date;


@Component
public class JwtTokenProvider {
    private final String secretKey = "your-very-secret-key"; // TODO: 나중에 외부 설정으로 빼기
    private final long accessTokenValidityInMillis = 1000L * 60 * 60; // 1시간
    private final UserRepository userRepository;

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenValidityInMillis);

        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("role", user.getRole().name())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(secretKey));

        return token;
    }

    public Long getUserIdFromToken(String token) {
        DecodedJWT decoded = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);

        return Long.parseLong(decoded.getSubject());
    }

    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 없음"));
        UserPrincipal principal = new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getNickname());

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }


    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
}
