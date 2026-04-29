package com.kakaotechcampus.journey_planner.global.common.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@UtilityClass
public class DateUtils {
    /**
     * @Param ttl: 실시간 시간으로부터 더하고자 하는 시간
     */
    public static Date convertInstantToDate(@NonNull Long ttl) {
        Instant nowUtc = Instant.now().plusMillis(ttl);
        ZonedDateTime now = nowUtc.atZone(ZoneId.of("Asia/Seoul"));
        return Date.from(now.toInstant());
    }

    public static Date now() {
        Instant nowUtc = Instant.now();
        ZonedDateTime now = nowUtc.atZone(ZoneId.of("Asia/Seoul"));
        return Date.from(now.toInstant());
    }
}
