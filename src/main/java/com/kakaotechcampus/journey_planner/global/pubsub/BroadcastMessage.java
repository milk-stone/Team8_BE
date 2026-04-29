package com.kakaotechcampus.journey_planner.global.pubsub;

import java.util.Map;

public record BroadcastMessage(String topic, Map<String, Object> payload) {
}
