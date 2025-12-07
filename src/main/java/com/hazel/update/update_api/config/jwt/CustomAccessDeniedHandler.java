package com.hazel.update.update_api.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getCode(), errorCode.getMessage());
        ApiResponse<?> apiResponse = ApiResponse.fail(errorResponse);

        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }

}
