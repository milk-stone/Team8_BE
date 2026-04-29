package com.kakaotechcampus.journey_planner.presentation.waypoint.dto.request;

import com.kakaotechcampus.journey_planner.domain.waypoint.LocationCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record WaypointRequest(
        String name,

        String description,

        String address,

        LocalDateTime startTime,

        LocalDateTime endTime,

        LocationCategory locationCategory,

        Float xPosition,

        Float yPosition
) {
}