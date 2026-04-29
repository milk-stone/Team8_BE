package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.route.VehicleCategory;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

public class RouteTestBuilder {
    private Long id = 1L;
    private Plan plan = mock(Plan.class);
    private Waypoint fromWayPoint = mock(Waypoint.class);
    private Waypoint toWayPoint = mock(Waypoint.class);
    private VehicleCategory vehicleCategory = VehicleCategory.CAR;
    private String title = "기본 경로 (A -> B)";
    private String description = "테스트를 위한 기본 경로 설명입니다.";
    private Float durationMin = 30.0f;

    public static RouteTestBuilder aRoute() {
        return new RouteTestBuilder();
    }

    public RouteTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RouteTestBuilder withPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public RouteTestBuilder withFromWayPoint(Waypoint fromWayPoint) {
        this.fromWayPoint = fromWayPoint;
        return this;
    }

    public RouteTestBuilder withToWayPoint(Waypoint toWayPoint) {
        this.toWayPoint = toWayPoint;
        return this;
    }

    public RouteTestBuilder withVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
        return this;
    }

    public RouteTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public RouteTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public RouteTestBuilder withDurationMin(Float durationMin) {
        this.durationMin = durationMin;
        return this;
    }

    public Route build() {
        Route route = new Route(
                this.plan,
                this.fromWayPoint,
                this.toWayPoint,
                this.title,
                this.description,
                this.durationMin,
                this.vehicleCategory
        );

        try {
            Field idField = Route.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(route, this.id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 id 필드 설정 실패", e);
        }

        return route;
    }
}
