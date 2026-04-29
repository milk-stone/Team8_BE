package com.kakaotechcampus.journey_planner.presentation.plan;

import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.CreatePlanRequest;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.CanvasResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.InvitationResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.PlanResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.UpdatePlanRequest;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans")
public class PlanController {
    private final PlanService planService;

    // Plan 생성
    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(
            @LoginMember Long memberId,
            @Valid @RequestBody CreatePlanRequest request
    ) {
        PlanResponse response = planService.createPlan(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Plan 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlan(
            @LoginMember Long memberId,
            @PathVariable Long id
    ) {
        PlanResponse response = planService.getPlan(memberId, id);
        return ResponseEntity.ok().body(response);
    }

    // Plan 전체 조회
    @GetMapping
    public ResponseEntity<Slice<PlanResponse>> getAllPlans(@LoginMember Long memberId, Pageable pageable) {
        Slice<PlanResponse> response = planService.getAllPlans(memberId, pageable);
        return ResponseEntity.ok().body(response);
    }

    // Plan 수정
    @PatchMapping("/{id}")
    public ResponseEntity<PlanResponse> updatePlan(
            @LoginMember Long memberId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlanRequest request
    ) {
        PlanResponse response = planService.updatePlan(memberId, id, request);
        return ResponseEntity.ok(response);
    }

    // Plan 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @LoginMember Long memberId,
            @PathVariable Long id
    ) {
        planService.deletePlan(memberId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/invitations/{planId}")
    public ResponseEntity<Void> inviteMember(
            @LoginMember Long memberId,
            @PathVariable Long planId,
            @RequestParam("email") String inviteeEmail
    ) {
        TravelerResponse response = planService.inviteMember(memberId, planId, inviteeEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @LoginMember Long memberId,
            @PathVariable Long invitationId
    ) {
        Traveler traveler = planService.acceptInvitation(memberId, invitationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<InvitationResponse>> getInvitations(@LoginMember Long memberId) {
        List<InvitationResponse> invitations = planService.getInvitations(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(invitations);
    }

    @GetMapping("/{planId}/canvas")
    public ResponseEntity<CanvasResponse> getCanvasData(@LoginMember Long memberId, @PathVariable Long planId) {
        CanvasResponse canvasResponse = planService.getCanvasData(memberId, planId);
        return ResponseEntity.status(HttpStatus.OK).body(canvasResponse);
    }
}
