package com.kakaotechcampus.journey_planner.global.interceptor;

import com.kakaotechcampus.journey_planner.application.token.TokenService;
import com.kakaotechcampus.journey_planner.domain.token.TokenType;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    static final String MEMBER_ID_KEY = "memberId";
    static final String TOKEN_EXPIRES_AT_KEY = "tokenExpiresAt";
    private final TokenService tokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (!StringUtils.hasText(authHeader)) {
            log.debug("[WS] Guest connection (no token)");
            return message;
        }

        if (!authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        String token = authHeader.substring(7);
        Long memberId = tokenService.getId(TokenType.ACCESS, token);
        long expiresAt = tokenService.getExpiresAt(TokenType.ACCESS, token);

        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs != null) {
            attrs.put(MEMBER_ID_KEY, memberId);
            attrs.put(TOKEN_EXPIRES_AT_KEY, expiresAt);
        }
        log.debug("[WS] Authenticated memberId={}", memberId);
        return message;
    }
}
