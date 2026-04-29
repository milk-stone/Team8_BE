package com.kakaotechcampus.journey_planner.application.gift;

import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.domain.gift.Gift;
import com.kakaotechcampus.journey_planner.domain.gift.GiftEvent;
import com.kakaotechcampus.journey_planner.domain.gift.repository.GiftEventRepository;
import com.kakaotechcampus.journey_planner.domain.gift.repository.GiftRepository;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.global.lock.DistributedLockService;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.request.CreateGiftEventRequest;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.response.GiftEventResponse;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.response.GiftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftEventService {

    private final GiftEventRepository giftEventRepository;
    private final GiftRepository giftRepository;
    private final PlanService planService;
    private final DistributedLockService lockService;

    @Transactional
    public GiftEventResponse createEvent(Long planId, Long memberId, CreateGiftEventRequest request) {
        Plan plan = planService.getPlanEntity(planId);
        if (!plan.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.GIFT_EVENT_ACCESS_DENIED);
        }
        GiftEvent event = GiftEvent.of(plan, memberId, request.totalCount(), request.content());
        return GiftEventResponse.from(giftEventRepository.save(event));
    }

    public GiftResponse claimGift(Long planId, Long eventId, Long memberId) {
        String lockKey = "lock:GIFT_EVENT:" + eventId;
        return lockService.executeWithLock(lockKey, () -> doClaim(planId, eventId, memberId));
    }

    @Transactional
    private GiftResponse doClaim(Long planId, Long eventId, Long memberId) {
        if (giftRepository.existsByGiftEventIdAndMemberId(eventId, memberId)) {
            throw new BusinessException(ErrorCode.GIFT_ALREADY_CLAIMED);
        }
        GiftEvent event = giftEventRepository.findByIdAndPlanId(eventId, planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GIFT_EVENT_NOT_FOUND));
        event.decreaseRemaining();
        Gift gift = giftRepository.save(Gift.of(event, memberId));
        return GiftResponse.from(gift);
    }

    @Transactional(readOnly = true)
    public List<GiftResponse> getMyGifts(Long memberId) {
        return giftRepository.findAllByMemberId(memberId).stream()
                .map(GiftResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GiftEventResponse getEvent(Long planId, Long eventId) {
        GiftEvent event = giftEventRepository.findByIdAndPlanId(eventId, planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GIFT_EVENT_NOT_FOUND));
        return GiftEventResponse.from(event);
    }
}
