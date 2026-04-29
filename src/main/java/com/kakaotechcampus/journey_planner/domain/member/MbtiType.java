package com.kakaotechcampus.journey_planner.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.INVALID_MBTITYPE;

public enum MbtiType {
    INFJ, INFP, INTJ, INTP, ISFJ, ISFP, ISTJ, ISTP, ENFJ, ENFP, ENTJ, ENTP, ESFJ, ESFP, ESTJ, ESTP;

    @JsonCreator
    public static MbtiType from(String s) {
        if (s == null) {
            return null;
        }
        try {
            return MbtiType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(INVALID_MBTITYPE);
        }
    }
}
