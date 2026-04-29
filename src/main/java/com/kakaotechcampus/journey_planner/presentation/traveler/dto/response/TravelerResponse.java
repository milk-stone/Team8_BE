package com.kakaotechcampus.journey_planner.presentation.traveler.dto.response;

import com.kakaotechcampus.journey_planner.domain.member.MbtiType;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.Role;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;

public record TravelerResponse(
        Long id,
        Long memberId,
        String name,
        String contact,
        MbtiType mbtiType,
        InvitationStatus status,
        Role role
) {
    public static TravelerResponse from(Traveler traveler) {
        return new TravelerResponse(
                traveler.getId(),
                traveler.getMember().getId(),
                traveler.getMember().getName(),
                traveler.getMember().getContact(),
                traveler.getMember().getMbtiType(),
                traveler.getStatus(),
                traveler.getRole()
        );
    }
}
