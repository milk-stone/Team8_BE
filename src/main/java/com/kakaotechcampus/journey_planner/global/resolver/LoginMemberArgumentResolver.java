package com.kakaotechcampus.journey_planner.global.resolver;

import com.kakaotechcampus.journey_planner.application.token.TokenService;
import com.kakaotechcampus.journey_planner.domain.token.TokenType;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new BusinessException(NO_TOKEN);
        }
        String token = extractToken(authorizationHeader);
        return tokenService.getId(TokenType.ACCESS, token);
    }

    private String extractToken(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("유효하지 않은 인증 헤더입니다.");
    }
}

