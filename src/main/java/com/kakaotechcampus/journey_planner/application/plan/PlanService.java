package com.kakaotechcampus.journey_planner.application.plan;

import com.kakaotechcampus.journey_planner.application.canvas.CanvasQueryService;
import com.kakaotechcampus.journey_planner.application.traveler.TravelerService;
import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.member.repository.MemberRepository;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.plan.PlanMapper;
import com.kakaotechcampus.journey_planner.domain.plan.repository.JpaPlanRepository;
import com.kakaotechcampus.journey_planner.domain.plan.repository.PlanRepository;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.domain.traveler.TravelerMapper;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.CreatePlanRequest;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.UpdatePlanRequest;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.CanvasResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.InvitationResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.PlanResponse;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final MemberRepository memberRepository;
    private final JpaPlanRepository jpaPlanRepository;
    private final PlanRepository planRepository;
    private final TravelerService travelerService;
    private final CanvasQueryService canvasQueryService;

    @Transactional
    public PlanResponse createPlan(Long memberId, CreatePlanRequest request) {
        Member member = getMember(memberId);
        Plan plan = PlanMapper.toEntity(request, member);
        Plan savedPlan = planRepository.save(plan);

        Traveler savedTraveler = travelerService.createOwnerTraveler(member, savedPlan);
        member.addTraveler(savedTraveler);
        plan.addTraveler(savedTraveler);

        return PlanResponse.of(savedPlan);
    }

    @Transactional(readOnly = true)
    public PlanResponse getPlan(Long memberId, Long planId) {
        Plan plan = checkPlanAccess(memberId, planId);
        return PlanResponse.of(plan);
    }

    private Plan checkPlanAccess(Long memberId, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));
        boolean isMemberInPlan = plan.hasMember(getMember(memberId));

        if (!isMemberInPlan) {
            throw new BusinessException(PLAN_ACCESS_DENIED);
        }
        return plan;
    }

    @Transactional(readOnly = true)
    public Slice<PlanResponse> getAllPlans(Long memberId, Pageable pageable) {
        return PlanResponse.toPagination(jpaPlanRepository.findAllByMemberId(memberId, pageable));
    }

    @Transactional
    public PlanResponse updatePlan(Long memberId, Long id, UpdatePlanRequest request) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));
        boolean isMemberInPlan = plan.hasMember(getMember(memberId));
        if (!isMemberInPlan) {
            throw new BusinessException(PLAN_ACCESS_DENIED);
        }
        plan.update(request);
        return PlanResponse.of(plan);
    }

    @Transactional
    public void deletePlan(Long memberId, Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));
        boolean isMemberInPlan = plan.hasMember(getMember(memberId));
        if (!isMemberInPlan) {
            throw new BusinessException(PLAN_ACCESS_DENIED);
        }
        planRepository.delete(plan);
    }

    @Transactional(readOnly = true)
    public Plan getPlanEntity(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));
    }

    @Transactional
    public TravelerResponse inviteMember(Long memberId, Long planId, String inviteeEmail) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));
        Member inviter = getMember(memberId);
        if (!travelerService.isOwner(plan, inviter)) {
            throw new BusinessException(PLAN_ACCESS_DENIED);
        }

        Member invitee = memberRepository.findByEmail(inviteeEmail)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        boolean alreadyExists = plan.hasMember(invitee);
        if (alreadyExists) {
            throw new BusinessException(MEMBER_ALREADY_IN_PLAN);
        }

        Traveler savedTraveler = travelerService.addTraveler(Traveler.createInvitation(invitee, plan));
        return TravelerMapper.toResponse(savedTraveler);
    }

    @Transactional
    public Traveler acceptInvitation(Long memberId, Long invitationId) {
        Traveler invitation = travelerService.getTraveler(invitationId);
        if (!invitation.getMember().equals(getMember(memberId))) {
            throw new BusinessException(INVITATION_ACCESS_DENIED);
        }

        if (invitation.getStatus() != InvitationStatus.INVITED) {
            throw new BusinessException(INVITATION_ALREADY_PROCESSED);
        }

        invitation.accept();
        return invitation;
    }

    @Transactional(readOnly = true)
    public List<InvitationResponse> getInvitations(Long memberId) {
        return TravelerMapper.toInvitationResponse(
                travelerService.getInvitations(getMember(memberId))
        );
    }

    @Transactional(readOnly = true)
    public List<TravelerResponse> getInvitedTravelers(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(PLAN_NOT_FOUND));

        return travelerService.getTravelers(plan);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public CanvasResponse getCanvasData(Long memberId, Long planId) {
        checkPlanAccess(memberId, planId);
        return canvasQueryService.getCanvasData(planId);
    }
}
