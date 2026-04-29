package com.kakaotechcampus.journey_planner.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // * GLOBAL

    // * PLAN
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PE_001", "해당 계획을 찾을 수 없습니다."),
    PLAN_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "PE_002", "해당 계획에 접근할 권한이 없습니다."),
    MEMBER_ALREADY_IN_PLAN(HttpStatus.BAD_REQUEST, "PE_003", "해당 계획에 이미 참여 중입니다."),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PE_004", "해당 초대장이 존재하지 않습니다."),
    INVITATION_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "PE_005", "해당 초대장에 접근할 권한이 없습니다."),
    INVITATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "PE_006", "이미 승인된 초대장 입니다."),

    // * Resource
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RE_001", "리소스를 찾을 수 없습니다."),
    FONT_NOT_FOUND(HttpStatus.NOT_FOUND, "RE_002", "폰트를 찾을 수 없습니다."),

    // Waypoint
    WAYPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "WE_001", "해당 웨이포인트를 찾을 수 없습니다."),
    INVALID_LOCATION_CATEGORY(HttpStatus.BAD_REQUEST, "WE_002", "잘못된 카테고리 입니다"),

    //Route
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "RE_001", "해당 경로를 찾을 수 없습니다."),

    // Memo
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "MEE_001", "해당 메모를 찾을 수 없습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ME_001", "해당 멤버를 찾을 수 없습니다."),
    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "ME_002", "이미 존재하는 멤버입니다."),
    INVALID_MBTITYPE(HttpStatus.BAD_REQUEST, "ME_003", "MBTI 타입이 올바르지 않습니다."),

    // Traveler
    TRAVELER_NOT_FOUND(HttpStatus.NOT_FOUND, "TE_001", "해당 여행자를 찾을 수 없습니다."),

    // Auth
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "AE_001", "로그인에 실패하였습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AE_002", "잘못된 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AE_003", "만료된 토큰입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "AE_004", "입력 형식이 잘못되었습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "AE_005", "비밀번호가 잘못되었습니다."),
    NO_TOKEN(HttpStatus.UNAUTHORIZED, "AE_006", "토큰을 찾을 수 없습니다."),

    // * PDF
    CANNOT_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "PDE_001", "PDF 를 생성할 수 없습니다."),
    NO_FILE(HttpStatus.NOT_FOUND, "PDE_002","파일 경로를 찾을 수 없습니다."),

    // * Gift
    GIFT_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "GE_001", "해당 이벤트를 찾을 수 없습니다."),
    GIFT_SOLD_OUT(HttpStatus.CONFLICT, "GE_002", "선착순이 마감되었습니다."),
    GIFT_ALREADY_CLAIMED(HttpStatus.BAD_REQUEST, "GE_003", "이미 수령한 이벤트입니다."),
    GIFT_EVENT_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "GE_004", "이벤트 생성 권한이 없습니다."),

    // * Lock
    NODE_LOCK_FAILED(HttpStatus.CONFLICT, "LE_001", "현재 다른 사용자가 편집 중입니다.")
    ;
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
