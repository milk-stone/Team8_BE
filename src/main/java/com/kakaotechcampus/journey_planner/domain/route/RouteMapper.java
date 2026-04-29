package com.kakaotechcampus.journey_planner.domain.route;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import com.kakaotechcampus.journey_planner.presentation.route.dto.request.RouteRequest;
import com.kakaotechcampus.journey_planner.presentation.route.dto.response.RouteResponse;

import java.util.List;
import java.util.stream.Collectors;

public class RouteMapper {

    public static Route toEntity(Plan plan, Waypoint fromWaypoint, Waypoint toWaypoint, RouteRequest request) {
        return new Route(
                plan,
                fromWaypoint,
                toWaypoint,
                request.title(),
                request.description(),
                request.duration(),
                request.vehicleCategory()
        );
    }

    public static RouteResponse toResponse(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getFromWayPoint().getId(),
                route.getToWayPoint().getId(),
                route.getTitle(),
                route.getDescription(),
                route.getDurationMin(),
                route.getVehicleCategory()
        );
    }

    public static List<RouteResponse> toResponseList(List<Route> routes) {
        return routes.stream()
                .map(RouteMapper::toResponse)
                .collect(Collectors.toList());
    }
}
