package com.kakaotechcampus.journey_planner.presentation.plan.dto.response;

import com.kakaotechcampus.journey_planner.presentation.memo.dto.response.MemoResponse;
import com.kakaotechcampus.journey_planner.presentation.route.dto.response.RouteResponse;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.response.WaypointResponse;

import java.util.List;

public record CanvasResponse(
        List<WaypointResponse> waypoints,
        List<MemoResponse> memos,
        List<RouteResponse> routes,
        List<TravelerResponse> travelers
) {
}
