package com.kakaotechcampus.journey_planner.domain.plan;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.CreatePlanRequest;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.PlanResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PlanMapper {

    public static Plan toEntity(CreatePlanRequest request, Member author) {
        return new Plan(
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                author
        );
    }

    public static List<PlanResponse> toResponseList(List<Plan> plans) {
        return plans.stream()
                .map(PlanResponse::of)
                .collect(Collectors.toList());
    }
}
