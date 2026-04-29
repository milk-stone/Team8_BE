package com.kakaotechcampus.journey_planner.infra.token;

import com.kakaotechcampus.journey_planner.application.token.TokenService;
import com.kakaotechcampus.journey_planner.domain.token.TokenType;
import com.kakaotechcampus.journey_planner.global.common.utils.DateUtils;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import com.kakaotechcampus.journey_planner.domain.token.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;

@Component
public class TokenProvider implements TokenService {
    private final Map<TokenType, Token> tokens;

    // * identify -> 자기 자신을 가져온다는 거임
    // * Token getType 이 Key 가 됨.
    public TokenProvider(List<Token> tokens) {
        this.tokens = tokens.stream()
                .collect(Collectors.toMap(Token::getType, Function.identity()));
    }

    @Override
    public String generateToken(TokenType type, Long id) {
        Token token = tokens.get(type);
        return Jwts.builder()
                .subject(id.toString())
                .claim("tokenType", token.getType().toString())
                .issuedAt(token.getGeneratedDate())
                .expiration(token.getExpirationDate())
                .signWith(token.getSecretKey())
                .compact();
    }

    @Override
    public Long getId(TokenType type, String token) {
        Claims claims = getClaims(type, token);
        validateToken(claims, tokens.get(type).getType().toString());
        return Long.parseLong(claims.getSubject());
    }

    @Override
    public long getExpiresAt(TokenType type, String token) {
        Claims claims = getClaims(type, token);
        return claims.getExpiration().getTime();
    }

    private void validateToken(Claims claims, String expectedType) throws BusinessException {
        String actualType = claims.get("tokenType").toString();
        if (!actualType.equals(expectedType)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        if (claims.getExpiration().before(DateUtils.now())) {
            throw new BusinessException(TOKEN_EXPIRED);
        }
    }

    private Claims getClaims(TokenType type, String token) {
        try {
            return Jwts.parser()
                    .verifyWith(tokens.get(type).getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            throw new BusinessException(TOKEN_EXPIRED);

        } catch (JwtException e) {
            throw new BusinessException(INVALID_TOKEN);
        }
    }
}
