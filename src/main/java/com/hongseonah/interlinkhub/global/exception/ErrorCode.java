package com.hongseonah.interlinkhub.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다."),
    DUPLICATE_SYSTEM_CODE(HttpStatus.CONFLICT, "DUPLICATE_SYSTEM_CODE", "이미 등록된 시스템 코드입니다."),
    SYSTEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SYSTEM_NOT_FOUND", "시스템 정보를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}