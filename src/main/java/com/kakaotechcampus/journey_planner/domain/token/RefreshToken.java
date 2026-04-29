package com.kakaotechcampus.journey_planner.domain.token;

import com.kakaotechcampus.journey_planner.global.common.utils.DateUtils;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public final class RefreshToken implements Token {
    private final SecretKey secretKey;
    private final Long expiration;

    public RefreshToken(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.refresh-token-expiration-in-ms}") Long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public TokenType getType() {
        return TokenType.REFRESH;
    }

    @Override
    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    @Override
    public Date getGeneratedDate() {
        return DateUtils.now();
    }

    @Override
    public Date getExpirationDate() {
        return DateUtils.convertInstantToDate(this.expiration);
    }
}
