package com.kakaotechcampus.journey_planner.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(@NotBlank String refreshToken) {
}
