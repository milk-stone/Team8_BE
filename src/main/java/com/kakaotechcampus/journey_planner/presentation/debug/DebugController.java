package com.kakaotechcampus.journey_planner.presentation.debug;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debug")
public class DebugController {

    // sessionId -> 구독 목록
    private final ConcurrentHashMap<String, List<String>> sessionSubscriptions = new ConcurrentHashMap<>();

    @EventListener
    public void onConnect(SessionConnectedEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        if (sessionId != null) {
            sessionSubscriptions.put(sessionId, new ArrayList<>());
        }
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        if (sessionId != null && destination != null) {
            sessionSubscriptions.computeIfPresent(sessionId, (k, subs) -> {
                subs.add(destination);
                return subs;
            });
        }
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String subscriptionId = accessor.getSubscriptionId();
        if (sessionId != null && subscriptionId != null) {
            sessionSubscriptions.computeIfPresent(sessionId, (k, subs) -> {
                subs.remove(subscriptionId);
                return subs;
            });
        }
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        if (sessionId != null) {
            sessionSubscriptions.remove(sessionId);
        }
    }

    @GetMapping("/ws/status")
    public Map<String, Object> getWebSocketStatus() {
        List<Map<String, Object>> sessions = sessionSubscriptions.entrySet().stream()
                .map(entry -> Map.of(
                        "sessionId", entry.getKey(),
                        "subscriptions", entry.getValue()
                ))
                .toList();

        return Map.of(
                "connectedSessionCount", sessionSubscriptions.size(),
                "sessions", sessions
        );
    }
}
