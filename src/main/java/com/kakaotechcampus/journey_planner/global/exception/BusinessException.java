package com.kakaotechcampus.journey_planner.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String message;


    public BusinessException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, Object... cause) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = String.format(errorCode.getMessage(), cause);
    }
}
