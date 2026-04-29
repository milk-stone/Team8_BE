package com.kakaotechcampus.journey_planner.presentation.member.dto.request;

import com.kakaotechcampus.journey_planner.domain.member.MbtiType;

public record ModifyMemberRequest(
        String email,
        String contact,
        String username,
        MbtiType mbti
) {
}
