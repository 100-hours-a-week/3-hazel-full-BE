package com.hazel.update.update_api.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorCode errorCode = ErrorCode.AUTHENTICATION_REQUIRED;
        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getCode(), errorCode.getMessage());
        ApiResponse<?> apiResponse = ApiResponse.fail(errorResponse);

        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);

    }
}
