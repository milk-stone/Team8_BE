package com.kakaotechcampus.journey_planner.presentation.gift.dto.response;

import com.kakaotechcampus.journey_planner.domain.gift.Gift;

import java.time.LocalDateTime;

public record GiftResponse(
        Long id,
        Long giftEventId,
        Long memberId,
        LocalDateTime issuedAt
) {
    public static GiftResponse from(Gift g) {
        return new GiftResponse(
                g.getId(),
                g.getGiftEvent().getId(),
                g.getMemberId(),
                g.getIssuedAt()
        );
    }
}
