package com.kakaotechcampus.journey_planner.application.member;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.member.repository.MemberRepository;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.ModifyMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.QuitMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.response.GetMember200Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaotechcampus.journey_planner.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<GetMember200Response> getAllMember(Pageable pageable) {
        return memberRepository.findAll(pageable).stream()
                .map(GetMember200Response::to)
                .toList();
    }

    @Transactional
    public void deleteById(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    public GetMember200Response modifyMember(Long memberId, ModifyMemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.modify(request);
        return GetMember200Response.to(member);
    }

    @Transactional(readOnly = true)
    public GetMember200Response getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        return GetMember200Response.to(member);
    }

    @Transactional
    public void quitMember(Long memberId, QuitMemberRequest request) throws BusinessException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        if (!member.verifyPassword(request.password(), passwordEncoder)) {
            throw new BusinessException(WRONG_PASSWORD);
        }
        deleteById(memberId);
    }
}
