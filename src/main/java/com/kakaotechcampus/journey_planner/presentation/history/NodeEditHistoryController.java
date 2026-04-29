package com.kakaotechcampus.journey_planner.presentation.history;

import com.kakaotechcampus.journey_planner.application.history.NodeEditHistoryService;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.repository.TravelerRepository;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.presentation.history.dto.NodeEditHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans/{planId}/nodes/{nodeId}/history")
public class NodeEditHistoryController {

    private final NodeEditHistoryService historyService;
    private final TravelerRepository travelerRepository;

    @GetMapping
    public ResponseEntity<List<NodeEditHistoryResponse>> getHistory(
            @LoginMember Long memberId,
            @PathVariable Long planId,
            @PathVariable Long nodeId,
            @RequestParam NodeSort nodeSort
    ) {
        travelerRepository.findByPlanIdAndMemberIdAndStatus(planId, memberId, InvitationStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_ACCESS_DENIED));
        return ResponseEntity.ok(historyService.getHistory(nodeId, nodeSort));
    }
}
