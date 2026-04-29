package com.kakaotechcampus.journey_planner.presentation.member;

import com.kakaotechcampus.journey_planner.application.member.MemberService;
import com.kakaotechcampus.journey_planner.global.annotation.LoginMember;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.ModifyMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.QuitMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.response.GetMember200Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<GetMember200Response>> getAllMember(
            @LoginMember Long memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(memberService.getAllMember(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(
            @LoginMember Long memberId,
            @PathVariable Long id
    ) {
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<GetMember200Response> getMemberInformation(@LoginMember Long memberId) {
        return ResponseEntity.ok().body(memberService.getMemberById(memberId));
    }

    @GetMapping("/me/id")
    public ResponseEntity<Long> getMyId(@LoginMember Long memberId) {
        return ResponseEntity.ok(memberId);
    }

    @PatchMapping("/me")
    public ResponseEntity<GetMember200Response> updateMemberInformation(
            @LoginMember Long memberId,
            @RequestBody ModifyMemberRequest request
    ) {
        return ResponseEntity.ok().body(memberService.modifyMember(memberId, request));
    }

    @PostMapping("/me/withdraw")
    public ResponseEntity<String> withdrawMember(
            @LoginMember Long memberId,
            @RequestBody QuitMemberRequest quitMemberRequest
    ) {
        memberService.quitMember(memberId, quitMemberRequest);
        return ResponseEntity.noContent().build();
    }
}
