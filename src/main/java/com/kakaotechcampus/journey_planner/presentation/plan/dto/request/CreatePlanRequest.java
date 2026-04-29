package com.kakaotechcampus.journey_planner.presentation.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePlanRequest(
        @NotBlank(message = "제목은 비어 있을 수 없습니다.")
        String title,
        @NotBlank(message = "셜명은 비어 있을 수 없습니다.")
        String description,
        @NotNull(message = "시작일은 필수 값입니다.")
        LocalDate startDate,
        @NotNull(message = "종료일은 필수 값입니다.")
        LocalDate endDate
) {
}
