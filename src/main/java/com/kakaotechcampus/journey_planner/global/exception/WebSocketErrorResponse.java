package com.kakaotechcampus.journey_planner.global.exception;

public record WebSocketErrorResponse(
        String code,
        String message
) {
}
