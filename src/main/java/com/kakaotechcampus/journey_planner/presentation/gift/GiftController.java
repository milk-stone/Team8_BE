package com.kakaotechcampus.journey_planner.presentation.gift;

import com.kakaotechcampus.journey_planner.application.gift.GiftEventService;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.presentation.gift.dto.response.GiftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/gifts")
public class GiftController {

    private final GiftEventService giftEventService;

    @GetMapping("/my")
    public ResponseEntity<List<GiftResponse>> getMyGifts(@LoginMember Long memberId) {
        return ResponseEntity.ok(giftEventService.getMyGifts(memberId));
    }
}
