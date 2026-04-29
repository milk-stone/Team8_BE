package com.kakaotechcampus.journey_planner.presentation.route.dto.response;

import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.route.VehicleCategory;

public record RouteResponse(
        Long id,
        Long fromWaypointId,
        Long toWaypointId,
        String title,
        String description,
        Float duration,
        VehicleCategory vehicleCategory
) {
    public static RouteResponse from(Route r) {
        return new RouteResponse(
                r.getId(),
                r.getFromWayPoint().getId(),
                r.getToWayPoint().getId(),
                r.getTitle(),
                r.getDescription(),
                r.getDurationMin(),
                r.getVehicleCategory()
        );
    }
}
