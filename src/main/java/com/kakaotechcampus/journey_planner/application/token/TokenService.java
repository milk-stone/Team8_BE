package com.kakaotechcampus.journey_planner.application.token;

import com.kakaotechcampus.journey_planner.domain.token.TokenType;

public interface TokenService {
    String generateToken(TokenType type, Long id);

    Long getId(TokenType type, String token);

    long getExpiresAt(TokenType type, String token);
}
