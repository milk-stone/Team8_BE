package com.kakaotechcampus.journey_planner.global.auth;

import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.repository.TravelerRepository;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthGuard {

    private final TravelerRepository travelerRepository;

    public void requirePlanMember(Long memberId, Long planId, Long tokenExpiresAt) {
        if (memberId == null) {
            throw new BusinessException(ErrorCode.NO_TOKEN);
        }
        if (tokenExpiresAt != null && System.currentTimeMillis() > tokenExpiresAt) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }
        travelerRepository
                .findByPlanIdAndMemberIdAndStatus(planId, memberId, InvitationStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAN_ACCESS_DENIED));
    }
}
