package com.kakaotechcampus.journey_planner.presentation.history.dto;

import com.kakaotechcampus.journey_planner.domain.history.NodeEditHistory;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;

import java.time.LocalDateTime;

public record NodeEditHistoryResponse(
        Long id,
        Long nodeId,
        NodeSort nodeSort,
        Long memberId,
        LocalDateTime editedAt,
        String snapshot
) {
    public static NodeEditHistoryResponse from(NodeEditHistory h) {
        return new NodeEditHistoryResponse(
                h.getId(),
                h.getNodeId(),
                h.getNodeSort(),
                h.getMemberId(),
                h.getEditedAt(),
                h.getSnapshot()
        );
    }
}
