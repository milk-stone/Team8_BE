package com.kakaotechcampus.journey_planner.presentation.memo.dto.response;

import com.kakaotechcampus.journey_planner.domain.memo.Memo;

public record MemoResponse(
        Long id,
        String title,
        String content,
        Float xPosition,
        Float yPosition
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
                memo.getId(),
                memo.getTitle(),
                memo.getContent(),
                memo.getXPosition(),
                memo.getYPosition()
        );
    }
}
