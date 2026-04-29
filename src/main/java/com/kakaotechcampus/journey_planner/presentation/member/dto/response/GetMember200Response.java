package com.kakaotechcampus.journey_planner.presentation.member.dto.response;

import com.kakaotechcampus.journey_planner.domain.member.MbtiType;
import com.kakaotechcampus.journey_planner.domain.member.Member;

public record GetMember200Response(
        String email,
        String contact,
        String username,
        MbtiType mbti
) {
    public static GetMember200Response to(Member member) {
        return new GetMember200Response(
                member.getEmail(),
                member.getContact(),
                member.getName(),
                member.getMbtiType()
        );
    }
}
