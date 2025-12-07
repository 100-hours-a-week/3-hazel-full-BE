package com.hazel.update.update_api.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int code;
    private String message;

    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse from(int code, String message) {
        return new ErrorResponse(code, message);
    }
}
