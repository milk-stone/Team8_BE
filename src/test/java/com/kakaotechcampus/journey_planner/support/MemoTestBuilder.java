package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

public class MemoTestBuilder {
    private Long id = 1L;
    private String title = "테스트 메모 제목";
    private String content = "테스트 메모 내용입니다.";
    private Float xPosition = 127.0f;
    private Float yPosition = 37.5f;

    private Plan plan = mock(Plan.class);
    private Waypoint waypoint = null;
    private Route route = null;

    public static MemoTestBuilder aMemo() {
        return new MemoTestBuilder();
    }

    public MemoTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MemoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public MemoTestBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public MemoTestBuilder withXPosition(Float xPosition) {
        this.xPosition = xPosition;
        return this;
    }

    public MemoTestBuilder withYPosition(Float yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    public MemoTestBuilder withPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public MemoTestBuilder withWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
        return this;
    }

    public MemoTestBuilder withRoute(Route route) {
        this.route = route;
        return this;
    }

    public Memo build() {
        Memo memo = new Memo(this.title, this.content, this.xPosition, this.yPosition);

        memo.assignToPlan(this.plan);
        memo.assignToWayPoint(this.waypoint);
        memo.assignToRoute(this.route);

        try {
            Field idField = Memo.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(memo, this.id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 id 필드 설정 실패", e);
        }

        return memo;
    }
}
