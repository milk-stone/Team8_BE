package com.kakaotechcampus.journey_planner.global.resolver;

import com.kakaotechcampus.journey_planner.global.annotation.WsMember;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebSocketMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WsMember.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        Map<String, Object> attrs = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
        if (attrs == null) return null;
        return (Long) attrs.get("memberId");
    }
}
