package com.hazel.update.update_api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 400, "이미 사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, 400, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 401, "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, 400, "현재 비밀번호가 일치하지 않습니다."),
    SAME_AS_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, 400, "새 비밀번호는 현재 비밀번호와 달라야 합니다."),

    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 챌린지입니다."),
    ALREADY_JOINED_CHALLENGE(HttpStatus.BAD_REQUEST, 400, "이미 참여 중인 챌린지입니다."),
    NOT_PARTICIPATING_CHALLENGE(HttpStatus.BAD_REQUEST, 400, "해당 챌린지에 참여 중인 사용자만 작성할 수 있습니다."),
    CHALLENGE_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, 403, "챌린지를 수정할 권한이 없습니다."),

    POST_FORBIDDEN(HttpStatus.FORBIDDEN, 403, "포스트를 수정할 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 포스트입니다."),
    DUPLICATED_LIKE(HttpStatus.BAD_REQUEST, 400, "이미 좋아요한 게시글입니다."),

    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,401, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,401, "만료된 리프레시 토큰입니다.");


    private final HttpStatus status;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}