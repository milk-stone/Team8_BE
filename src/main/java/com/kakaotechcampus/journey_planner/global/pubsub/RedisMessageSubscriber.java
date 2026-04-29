package com.kakaotechcampus.journey_planner.global.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    public void onMessage(String message) {
        try {
            BroadcastMessage broadcastMessage = objectMapper.readValue(message, BroadcastMessage.class);
            simpMessagingTemplate.convertAndSend(broadcastMessage.topic(), broadcastMessage.payload());
            log.debug("[Pub/Sub] 수신 및 broadcast: {}", broadcastMessage.topic());
        } catch (JsonProcessingException e) {
            log.error("[Pub/Sub] 메시지 역직렬화 실패: {}", e.getMessage());
        }
    }
}
