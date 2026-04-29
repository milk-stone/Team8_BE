package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.member.MemberService;
import com.kakaotechcampus.journey_planner.domain.member.MbtiType;
import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.member.repository.MemberRepository;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.ModifyMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.QuitMemberRequest;
import com.kakaotechcampus.journey_planner.presentation.member.dto.response.GetMember200Response;
import com.kakaotechcampus.journey_planner.support.MemberTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("전체 회원 조회(getAllMember) 테스트")
    class GetAllMemberTest {
        @Test
        @DisplayName("성공")
        void getAllMemberSuccess() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            Member member1 = MemberTestBuilder.aMember().withEmail("test1@example.com").build();
            Member member2 = MemberTestBuilder.aMember().withEmail("test2@example.com").build();
            given(memberRepository.findAll(pageable)).willReturn(new PageImpl<>(List.of(member1, member2)));

            // when
            List<GetMember200Response> responses = memberService.getAllMember(pageable);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).email()).isEqualTo("test1@example.com");
            assertThat(responses.get(1).email()).isEqualTo("test2@example.com");
        }
    }

    @Nested
    @DisplayName("회원 정보 수정(modifyMember) 테스트")
    class ModifyMemberTest {
        @Test
        @DisplayName("성공")
        void modifyMemberSuccess() {
            // given
            Long memberId = 1L;
            ModifyMemberRequest request = new ModifyMemberRequest("after@example.com", "010-9999-9999", "새이름", MbtiType.ISFJ);
            Member member = MemberTestBuilder.aMember()
                    .withId(memberId)
                    .withEmail("before@example.com")
                    .withContact("010-1111-1111")
                    .withName("기존이름")
                    .withMbti("ENTP").build();
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            // when
            GetMember200Response response = memberService.modifyMember(memberId, request);

            // then
            assertThat(response.email()).isEqualTo("after@example.com");
            assertThat(response.contact()).isEqualTo("010-9999-9999");
            assertThat(response.username()).isEqualTo("새이름");
            assertThat(response.mbti().toString().toUpperCase()).isEqualTo("ISFJ");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 회원")
        void modifyMemberFail_MemberNotFound() {
            // given
            Long memberId = 999L;
            ModifyMemberRequest request = new ModifyMemberRequest("after@example.com", "010-9999-9999", "새이름", MbtiType.ISFJ);
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    memberService.modifyMember(memberId, request)
            );
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("ME_001");
            assertThat(exception.getMessage()).isEqualTo("해당 멤버를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("회원 탈퇴(quitMember) 테스트")
    class QuitMemberTest {
        @Test
        @DisplayName("성공")
        void quitMemberSuccess() {
            // given
            Long memberId = 1L;
            QuitMemberRequest request = new QuitMemberRequest("password123");
            Member member = MemberTestBuilder.aMember()
                    .withId(memberId)
                    .withPassword("encodedPassword").build();

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(passwordEncoder.matches("password123", member.getPassword())).willReturn(true);
            willDoNothing().given(memberRepository).deleteById(memberId);

            // when
            memberService.quitMember(memberId, request);

            // then
            then(memberRepository).should(times(1)).deleteById(memberId);
        }

        @Test
        @DisplayName("실패 - 비밀번호 불일치")
        void quitMemberFail_WrongPassword() {
            // given
            Long memberId = 1L;
            QuitMemberRequest request = new QuitMemberRequest("wrongPassword");
            Member member = MemberTestBuilder.aMember().withId(memberId).build();

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(passwordEncoder.matches("wrongPassword", member.getPassword())).willReturn(false);

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    memberService.quitMember(memberId, request)
            );
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(exception.getCode()).isEqualTo("AE_005");
            assertThat(exception.getMessage()).isEqualTo("비밀번호가 잘못되었습니다.");
        }
    }
}
