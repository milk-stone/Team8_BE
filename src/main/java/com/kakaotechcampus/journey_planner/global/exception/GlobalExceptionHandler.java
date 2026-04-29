package com.kakaotechcampus.journey_planner.global.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String detailMessage = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), detailMessage));
    }
}
