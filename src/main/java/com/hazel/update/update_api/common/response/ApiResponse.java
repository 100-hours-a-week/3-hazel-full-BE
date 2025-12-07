package com.hazel.update.update_api.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private String result;
    private T data;
    private ErrorResponse error;

    private ApiResponse(String result, T data, ErrorResponse error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, null);
    }

    public static <T> ApiResponse<T> fail(ErrorResponse error) {
        return new ApiResponse<>("fail", null, error);
    }

}
