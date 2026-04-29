package com.kakaotechcampus.journey_planner.presentation.route;

import com.kakaotechcampus.journey_planner.application.message.MessageService;
import com.kakaotechcampus.journey_planner.application.route.RouteService;
import com.kakaotechcampus.journey_planner.global.annotation.WsExpiresAt;
import com.kakaotechcampus.journey_planner.global.annotation.WsMember;
import com.kakaotechcampus.journey_planner.global.auth.WebSocketAuthGuard;
import com.kakaotechcampus.journey_planner.global.lock.DistributedLockService;
import com.kakaotechcampus.journey_planner.presentation.route.dto.request.RouteRequest;
import com.kakaotechcampus.journey_planner.presentation.route.dto.response.RouteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.kakaotechcampus.journey_planner.domain.message.MessageType.ROUTE;

@Slf4j
@Controller
@RequiredArgsConstructor
@MessageMapping("/plans/{planId}/routes")
public class RouteController {

    private final RouteService routeService;
    private final MessageService messageService;
    private final DistributedLockService lockService;
    private final WebSocketAuthGuard authGuard;
    private static final String DESTINATION = "routes";

    @MessageMapping("/init")
    public void initRoutes(@DestinationVariable Long planId) {
        List<RouteResponse> routeResponses = routeService.getRoutes(planId);
        messageService.sendInitMessage(ROUTE, planId, DESTINATION, routeResponses);
    }

    @MessageMapping("/create")
    public void createRoute(
            @DestinationVariable Long planId,
            @Valid @Payload RouteRequest request,
            @Header("simpSessionId") String sessionId,
            @WsMember Long memberId,
            @WsExpiresAt Long tokenExpiresAt
    ) {
        authGuard.requirePlanMember(memberId, planId, tokenExpiresAt);
        RouteResponse response = routeService.createRoute(planId, request, memberId);
        messageService.sendCreateMessage(ROUTE, planId, DESTINATION, response);
    }

    // 수정 (자기 세션 제외 브로드캐스트)
    @MessageMapping("/{routeId}/update")
    public void updateRoute(
            @DestinationVariable Long planId,
            @DestinationVariable Long routeId,
            @Valid @Payload RouteRequest request,
            @Header("simpSessionId") String sessionId,
            @WsMember Long memberId,
            @WsExpiresAt Long tokenExpiresAt
    ) {
        authGuard.requirePlanMember(memberId, planId, tokenExpiresAt);
        long start = System.currentTimeMillis();
        String lockKey = "lock:ROUTE:" + routeId;
        RouteResponse response = lockService.executeWithLock(lockKey,
                () -> routeService.updateRoute(planId, routeId, request, memberId));
        messageService.sendUpdateMessage(ROUTE, planId, DESTINATION, response, sessionId);
        log.info("[ROUTE UPDATE] planId={}, routeId={}, 처리시간={}ms", planId, routeId, System.currentTimeMillis() - start);
    }

    @MessageMapping("/{routeId}/delete")
    public void deleteRoute(
            @DestinationVariable Long planId,
            @DestinationVariable Long routeId,
            @WsMember Long memberId,
            @WsExpiresAt Long tokenExpiresAt
    ) {
        authGuard.requirePlanMember(memberId, planId, tokenExpiresAt);
        routeService.deleteRoute(planId, routeId);
        messageService.sendDeleteMessage(ROUTE, planId, DESTINATION, routeId);
    }
}
