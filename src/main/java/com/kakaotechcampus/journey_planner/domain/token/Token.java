package com.kakaotechcampus.journey_planner.domain.token;

import javax.crypto.SecretKey;
import java.util.Date;

public sealed interface Token permits AccessToken, RefreshToken {
    TokenType getType();

    SecretKey getSecretKey();

    // 생성시간으로 네이밍 설정
    Date getGeneratedDate();

    Date getExpirationDate();
}
