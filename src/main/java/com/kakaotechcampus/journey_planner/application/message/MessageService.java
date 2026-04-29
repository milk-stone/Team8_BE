package com.kakaotechcampus.journey_planner.application.message;

import com.kakaotechcampus.journey_planner.domain.message.MessageType;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendInitMessage(MessageType type, Long planId, String destination, Object message);

    void sendCreateMessage(MessageType type, Long planId, String destination, Object message);

    void sendDeleteMessage(MessageType type, Long planId, String destination, Object message);

    // ✅ senderSessionId 기반 메시지 (자기 자신 제외용)
    void sendUpdateMessage(MessageType type, Long planId, String destination, Object message, String senderSessionId);
}
