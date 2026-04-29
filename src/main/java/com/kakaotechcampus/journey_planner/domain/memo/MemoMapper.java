package com.kakaotechcampus.journey_planner.domain.memo;

import com.kakaotechcampus.journey_planner.presentation.memo.dto.request.MemoRequest;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.response.MemoResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MemoMapper {

    public static Memo toEntity(MemoRequest request) {
        return new Memo(
                request.title(),
                request.content(),
                request.xPosition(),
                request.yPosition()
        );
    }

    public static MemoResponse toResponse(Memo memo) {
        return new MemoResponse(
                memo.getId(),
                memo.getTitle(),
                memo.getContent(),
                memo.getXPosition(),
                memo.getYPosition()
        );
    }

    public static List<MemoResponse> toResponseList(List<Memo> memos) {
        return memos.stream()
                .map(MemoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
