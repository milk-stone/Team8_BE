package com.kakaotechcampus.journey_planner.application.memo;

import com.kakaotechcampus.journey_planner.application.history.NodeEditHistoryService;
import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.application.route.RouteService;
import com.kakaotechcampus.journey_planner.application.waypoint.WaypointService;
import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import com.kakaotechcampus.journey_planner.domain.memo.MemoMapper;
import com.kakaotechcampus.journey_planner.domain.memo.repository.MemoRepository;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.request.MemoRequest;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.response.MemoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final PlanService planService;
    private final WaypointService waypointService;
    private final RouteService routeService;
    private final NodeEditHistoryService historyService;

    @Transactional
    public MemoResponse createMemo(Long planId, MemoRequest request, Long memberId) {
        Memo memo = MemoMapper.toEntity(request);
        Plan plan = planService.getPlanEntity(planId);

        plan.addMemo(memo);
        assignTarget(memo, request);
        Memo savedMemo = memoRepository.save(memo);

        MemoResponse response = MemoMapper.toResponse(savedMemo);
        historyService.record(savedMemo.getId(), NodeSort.MEMO, memberId, response);
        return response;
    }

    @Transactional
    public MemoResponse updateMemo(Long planId, Long memoId, MemoRequest request, Long memberId) {
        Memo memo = memoRepository.findByIdAndPlanId(memoId, planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMO_NOT_FOUND));

        memo.update(
                request.title(),
                request.content(),
                request.xPosition(),
                request.yPosition()
        );

        assignTarget(memo, request);

        MemoResponse response = MemoMapper.toResponse(memo);
        historyService.record(memo.getId(), NodeSort.MEMO, memberId, response);
        return response;
    }

    @Transactional
    public void deleteMemo(Long planId, Long memoId) {
        Memo memo = memoRepository.findByIdAndPlanId(memoId, planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMO_NOT_FOUND));

        historyService.deleteHistory(memoId, NodeSort.MEMO);
        Plan plan = memo.getPlan();
        plan.removeMemo(memo); // Plan 컬렉션에서 제거 → orphanRemoval 덕분에 DB에서도 삭제
    }

    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(Long planId) {
        Plan plan = planService.getPlanEntity(planId);

        List<Memo> memos = memoRepository.findAllByPlanId(plan.getId());
        return MemoMapper.toResponseList(memos);
    }

    private void assignTarget(Memo memo, MemoRequest request) {
        if (request.waypointId() != null) {
            memo.assignToWayPoint(
                    waypointService.getWaypointEntity(request.waypointId())
            );
        } else if (request.routeId() != null) {
            memo.assignToRoute(
                    routeService.getRouteEntity(request.routeId())
            );
        } else {
            // 둘 다 null이면 Plan에만 속하는 Memo
            memo.assignToWayPoint(null);
            memo.assignToRoute(null);
        }
    }

}
