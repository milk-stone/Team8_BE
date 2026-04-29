package com.kakaotechcampus.journey_planner.presentation.auth.dto.request;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(
        @NotBlank(message = "이름은 비워둘 수 없습니다.")
        String name,
        @NotBlank(message = "전화번호는 비워둘 수 없습니다.")
        String contact,
        @NotBlank(message = "이메일은 비워둘 수 없습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
        String password,
        @NotBlank(message = "MBTI는 비워둘 수 없습니다.")
        String mbti
) {
    public Member toEntity() {
        return new Member(
                name,
                contact,
                email,
                password,
                mbti
        );
    }
}
