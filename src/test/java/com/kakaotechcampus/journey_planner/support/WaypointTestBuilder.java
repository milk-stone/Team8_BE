package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.waypoint.LocationCategory;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;

public class WaypointTestBuilder {
    private Long id = 1L;
    private Plan plan = mock(Plan.class);
    private String name = "경유지 테스트";
    private String description = "테스트를 위한 기본 경유지 설명입니다.";
    private String address = "서울시 강남구 테헤란로";
    private LocalDateTime startTime = LocalDateTime.of(2025, 10, 18, 10, 0, 0);
    private LocalDateTime endTime = LocalDateTime.of(2025, 10, 18, 12, 0, 0);
    private LocationCategory locationCategory = LocationCategory.DEFAULT;
    private Float xPosition = 127.0276f;
    private Float yPosition = 37.4979f;

    public static WaypointTestBuilder aWaypoint() {
        return new WaypointTestBuilder();
    }

    public WaypointTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WaypointTestBuilder withPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public WaypointTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public WaypointTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WaypointTestBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public WaypointTestBuilder withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public WaypointTestBuilder withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public WaypointTestBuilder withLocationCategory(LocationCategory locationCategory) {
        this.locationCategory = locationCategory;
        return this;
    }

    public WaypointTestBuilder withXPosition(Float xPosition) {
        this.xPosition = xPosition;
        return this;
    }

    public WaypointTestBuilder withYPosition(Float yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    public Waypoint build() {
        Waypoint waypoint = new Waypoint(
                this.name,
                this.description,
                this.address,
                this.startTime,
                this.endTime,
                this.locationCategory,
                this.xPosition,
                this.yPosition
        );

        waypoint.assignToPlan(this.plan);

        try {
            Field idField = Waypoint.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(waypoint, this.id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 id 필드 설정 실패", e);
        }

        return waypoint;
    }
}
