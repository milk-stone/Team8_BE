package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.Role;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

public class TravelerTestBuilder {
    private Long id = 1L;
    private Member member = mock(Member.class);
    private Plan plan = mock(Plan.class);
    private InvitationStatus status = InvitationStatus.ACCEPTED;
    private Role role = Role.OWNER;

    public static TravelerTestBuilder aTraveler() {
        return new TravelerTestBuilder();
    }

    public TravelerTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TravelerTestBuilder withMember(Member member) {
        this.member = member;
        return this;
    }

    public TravelerTestBuilder withPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public TravelerTestBuilder withStatus(InvitationStatus status) {
        this.status = status;
        return this;
    }

    public TravelerTestBuilder withRole(Role role) {
        this.role = role;
        return this;
    }

    public Traveler build() {
        Traveler traveler = Traveler.createPlan(this.member, this.plan);

        try {
            setField(traveler, "id", this.id);
            setField(traveler, "status", this.status);
            setField(traveler, "role", this.role);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 필드 설정 실패", e);
        }

        return traveler;
    }

    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = Traveler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
