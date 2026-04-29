package com.kakaotechcampus.journey_planner.presentation.memo.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemoRequest(

        String title,

        String content,

        Float xPosition,

        Float yPosition,

        Long waypointId,

        Long routeId
) {
}
