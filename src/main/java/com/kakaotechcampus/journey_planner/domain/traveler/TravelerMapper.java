package com.kakaotechcampus.journey_planner.domain.traveler;

import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.InvitationResponse;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TravelerMapper {
    public static List<InvitationResponse> toInvitationResponse(List<Traveler> travelers) {
        return travelers.stream()
                .map(traveler -> new InvitationResponse(
                        traveler.getPlan().getId(),
                        traveler.getPlan().getTitle(),
                        traveler.getId(),
                        traveler.getStatus().toString()
                ))
                .toList();
    }

    public static TravelerResponse toResponse(Traveler traveler) {
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

    public static List<TravelerResponse> toResponseList(List<Traveler> travelers) {
        return travelers.stream()
                .map(TravelerMapper::toResponse)
                .collect(Collectors.toList());
    }
}
