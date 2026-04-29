package com.kakaotechcampus.journey_planner.presentation.route.dto.request;


import com.kakaotechcampus.journey_planner.domain.route.VehicleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RouteRequest(
        Long fromWaypointId,
        Long toWaypointId,
        String title,
        String description,
        Float duration,
        VehicleCategory vehicleCategory
) {
}
