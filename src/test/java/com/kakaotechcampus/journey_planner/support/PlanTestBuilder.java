package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;

public class PlanTestBuilder {
    private Long id = 1L;
    private String title = "기본 테스트 여행";
    private String description = "테스트를 위한 기본 설명입니다.";
    private LocalDate startDate = LocalDate.of(2025, 10, 10);
    private LocalDate endDate = LocalDate.of(2025, 10, 13);

    private Member member = mock(Member.class);

    public static PlanTestBuilder aPlan() {
        return new PlanTestBuilder();
    }

    public PlanTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PlanTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PlanTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PlanTestBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public PlanTestBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public PlanTestBuilder withMember(Member member) {
        this.member = member;
        return this;
    }

    public Plan build() {
        Plan plan = new Plan(this.title, this.description, this.startDate, this.endDate, this.member);

        try {
            Field idField = Plan.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(plan, this.id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 id 필드 설정 실패", e);
        }

        return plan;
    }
}
