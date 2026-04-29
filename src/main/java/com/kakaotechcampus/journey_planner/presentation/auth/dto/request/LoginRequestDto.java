package com.kakaotechcampus.journey_planner.presentation.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "이메일은 비워둘 수 없습니다.")
        @Email(message = "이메일 형식을 지켜주세요.")
        String email,
        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
        String password
) {
}
