package com.kakaotechcampus.journey_planner.infra.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotechcampus.journey_planner.application.message.MessageService;
import com.kakaotechcampus.journey_planner.domain.message.MessageBehaviorType;
import com.kakaotechcampus.journey_planner.domain.message.MessageType;
import com.kakaotechcampus.journey_planner.global.pubsub.BroadcastMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kakaotechcampus.journey_planner.global.config.RedisPubSubConfig.BROADCAST_CHANNEL;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProvider implements MessageService {

    private static final String MESSAGE_PREFIX = "/topic/plans";
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendInitMessage(MessageType type, Long planId, String destination, Object message) {
        publish(type, planId, destination, MessageBehaviorType.INIT, message, null);
    }

    @Override
    public void sendCreateMessage(MessageType type, Long planId, String destination, Object message) {
        publish(type, planId, destination, MessageBehaviorType.CREATE, message, null);
    }

    @Override
    public void sendDeleteMessage(MessageType type, Long planId, String destination, Object message) {
        publish(type, planId, destination, MessageBehaviorType.DELETE, message, null);
    }

    @Override
    public void sendUpdateMessage(MessageType type, Long planId, String destination, Object message, String senderSessionId) {
        publish(type, planId, destination, MessageBehaviorType.UPDATE, message, senderSessionId);
    }

    private void publish(MessageType type, Long planId, String destination, MessageBehaviorType behaviorType, Object payload, String senderSessionId) {
        String topic = MESSAGE_PREFIX + "/" + planId + "/" + destination;
        Map<String, Object> payloadMap = buildPayload(type, behaviorType, payload, senderSessionId);
        try {
            String json = objectMapper.writeValueAsString(new BroadcastMessage(topic, payloadMap));
            stringRedisTemplate.convertAndSend(BROADCAST_CHANNEL, json);
            log.debug("[Pub/Sub] 발행: {}", topic);
        } catch (JsonProcessingException e) {
            log.error("[Pub/Sub] 메시지 직렬화 실패: {}", e.getMessage());
        }
    }

    private Map<String, Object> buildPayload(MessageType type, MessageBehaviorType behaviorType, Object payload, String senderSessionId) {
        if (senderSessionId != null) {
            return Map.of(
                    "type", behaviorType.name(),
                    "senderSessionId", senderSessionId,
                    type.name(), payload
            );
        }
        return Map.of(
                "type", behaviorType.name(),
                type.name(), payload
        );
    }
}
