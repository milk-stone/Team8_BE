package com.kakaotechcampus.journey_planner.presentation.waypoint.dto.response;

import com.kakaotechcampus.journey_planner.domain.waypoint.LocationCategory;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;

import java.time.LocalDateTime;

public record WaypointResponse(
        Long id,

        String name,
        String description,

        String address,

        LocalDateTime startTime,
        LocalDateTime endTime,

        LocationCategory locationCategory,

        Float xPosition,
        Float yPosition
) {
    public static WaypointResponse from(Waypoint waypoint) {
        return new WaypointResponse(
                waypoint.getId(),
                waypoint.getName(),
                waypoint.getDescription(),
                waypoint.getAddress(),
                waypoint.getStartTime(),
                waypoint.getEndTime(),
                waypoint.getLocationCategory(),
                waypoint.getXPosition(),
                waypoint.getYPosition()
        );
    }
}