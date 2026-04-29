package com.kakaotechcampus.journey_planner.domain.waypoint;

import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.request.WaypointRequest;
import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.response.WaypointResponse;

import java.util.List;
import java.util.stream.Collectors;

public class WaypointMapper {

    public static Waypoint toEntity(WaypointRequest request) {
        return new Waypoint(
                request.name(),
                request.description(),
                request.address(),
                request.startTime(),
                request.endTime(),
                request.locationCategory(),
                request.xPosition(),
                request.yPosition()
        );
    }

    public static WaypointResponse toResponse(Waypoint waypoint) {
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

    public static List<WaypointResponse> toResponseList(List<Waypoint> waypoints) {
        return waypoints.stream()
                .map(WaypointMapper::toResponse)
                .collect(Collectors.toList());
    }
}
