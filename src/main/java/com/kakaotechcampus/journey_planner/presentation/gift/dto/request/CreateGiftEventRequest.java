package com.kakaotechcampus.journey_planner.presentation.gift.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateGiftEventRequest(
        @Min(1) int totalCount,
        @NotBlank String content
) {}
