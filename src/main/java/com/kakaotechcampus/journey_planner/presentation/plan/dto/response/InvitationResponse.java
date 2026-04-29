package com.kakaotechcampus.journey_planner.presentation.plan.dto.response;

public record InvitationResponse(
        Long planId,
        String planTitle,
        Long invitationId,
        String status
) {
}
