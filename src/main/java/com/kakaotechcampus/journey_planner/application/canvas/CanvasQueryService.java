package com.kakaotechcampus.journey_planner.application.canvas;

import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import com.kakaotechcampus.journey_planner.domain.memo.repository.MemoRepository;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.route.repository.RouteRepository;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.domain.traveler.repository.TravelerRepository;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import com.kakaotechcampus.journey_planner.domain.waypoint.repository.WaypointRepository;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.response.MemoResponse;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.response.CanvasResponse;
import com.kakaotechcampus.journey_planner.presentation.route.dto.response.RouteResponse;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.response.WaypointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CanvasQueryService {

    private final WaypointRepository waypointRepository;
    private final MemoRepository memoRepository;
    private final RouteRepository routeRepository;
    private final TravelerRepository travelerRepository;

    public CanvasResponse getCanvasData(Long planId) {
        List<Waypoint> waypoints = waypointRepository.findAllByPlanId(planId);
        List<Memo> memos = memoRepository.findAllByPlanId(planId);
        List<Route> routes = routeRepository.findAllByPlanId(planId);
        List<Traveler> travelers = travelerRepository.findAllByPlanId(planId);

        List<WaypointResponse> waypointDtos = waypoints.stream()
                .map(WaypointResponse::from)
                .toList();

        List<MemoResponse> memoDtos = memos.stream()
                .map(MemoResponse::from)
                .toList();

        List<RouteResponse> routeDtos = routes.stream()
                .map(RouteResponse::from)
                .toList();

        List<TravelerResponse> travelerDtos = travelers.stream()
                .map(TravelerResponse::from)
                .toList();

        return new CanvasResponse(waypointDtos, memoDtos, routeDtos, travelerDtos);
    }
}
