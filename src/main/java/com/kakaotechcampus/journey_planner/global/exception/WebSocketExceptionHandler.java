package com.kakaotechcampus.journey_planner.global.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Objects;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler({BusinessException.class})
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public WebSocketErrorResponse handleBusinessException(BusinessException e) {

        return new WebSocketErrorResponse(e.getCode(), e.getMessage());
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public WebSocketErrorResponse handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = Objects.requireNonNull(e.getBindingResult().getAllErrors())
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("유효성 검사 실패");

        return new WebSocketErrorResponse("VALIDATION_ERROR", errorMessage);
    }
}
