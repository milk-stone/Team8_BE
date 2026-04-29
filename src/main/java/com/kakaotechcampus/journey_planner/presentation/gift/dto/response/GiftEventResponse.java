package com.kakaotechcampus.journey_planner.presentation.gift.dto.response;

import com.kakaotechcampus.journey_planner.domain.gift.GiftEvent;

public record GiftEventResponse(
        Long id,
        Long planId,
        int totalCount,
        int remainingCount,
        String content
) {
    public static GiftEventResponse from(GiftEvent e) {
        return new GiftEventResponse(
                e.getId(),
                e.getPlan().getId(),
                e.getTotalCount(),
                e.getRemainingCount(),
                e.getContent()
        );
    }
}
