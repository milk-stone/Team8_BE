package com.kakaotechcampus.journey_planner.presentation.gift;

import com.kakaotechcampus.journey_planner.application.gift.GiftEventService;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.repository.TravelerRepository;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.request.CreateGiftEventRequest;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.response.GiftEventResponse;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.response.GiftResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plans/{planId}/gift-events")
public class GiftEventController {

    private final GiftEventService giftEventService;
    private final TravelerRepository travelerRepository;

    @PostMapping
    public ResponseEntity<GiftEventResponse> createEvent(
            @LoginMember Long memberId,
            @PathVariable Long planId,
            @Valid @RequestBody CreateGiftEventRequest request) {
        return ResponseEntity.ok(giftEventService.createEvent(planId, memberId, request));
    }

    @PostMapping("/{eventId}/claim")
    public ResponseEntity<GiftResponse> claimGift(
            @LoginMember Long memberId,
            @PathVariable Long planId,
            @PathVariable Long eventId) {
        travelerRepository.findByPlanIdAndMemberIdAndStatus(planId, memberId, InvitationStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_ACCESS_DENIED));
        return ResponseEntity.ok(giftEventService.claimGift(planId, eventId, memberId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GiftEventResponse> getEvent(
            @PathVariable Long planId,
            @PathVariable Long eventId) {
        return ResponseEntity.ok(giftEventService.getEvent(planId, eventId));
    }
}
