package com.hazel.update.update_api.common.exception;

import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.from(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        ApiResponse<?> apiResponse = ApiResponse.fail(errorResponse);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(apiResponse);
    }
}
