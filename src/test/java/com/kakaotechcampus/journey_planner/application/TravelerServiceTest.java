package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.traveler.TravelerService;
import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.Role;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.domain.traveler.repository.TravelerRepository;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import com.kakaotechcampus.journey_planner.support.MemberTestBuilder;
import com.kakaotechcampus.journey_planner.support.PlanTestBuilder;
import com.kakaotechcampus.journey_planner.support.TravelerTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TravelerServiceTest {
    @InjectMocks
    private TravelerService travelerService;

    @Mock
    private TravelerRepository travelerRepository;

    @Nested
    @DisplayName("소유주 여행자 생성(createOwnerTraveler) 테스트")
    class CreateOwnerTravelerTest {
        @Test
        @DisplayName("성공")
        void createOwnerTravelerSuccess() {
            // given
            Member member = MemberTestBuilder.aMember().build();
            Plan plan = PlanTestBuilder.aPlan().build();
            Traveler savedTraveler = TravelerTestBuilder.aTraveler().build();
            given(travelerRepository.save(any(Traveler.class))).willReturn(savedTraveler);

            // when
            Traveler result = travelerService.createOwnerTraveler(member, plan);

            // then
            assertThat(result).isEqualTo(savedTraveler);
            // ArgumentCaptor를 사용하여 repository.save()에 전달된 Traveler 객체를 캡처
            ArgumentCaptor<Traveler> travelerCaptor = ArgumentCaptor.forClass(Traveler.class);
            then(travelerRepository).should(times(1)).save(travelerCaptor.capture());
            assertThat(travelerCaptor.getValue().getRole()).isEqualTo(Role.OWNER);
            assertThat(travelerCaptor.getValue().getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
        }
    }

    @Nested
    @DisplayName("여행자 추가(addTraveler) 테스트")
    class AddTravelerTest {
        @Test
        @DisplayName("성공")
        void addTravelerSuccess() {
            // given
            Traveler traveler = TravelerTestBuilder.aTraveler().build();
            given(travelerRepository.save(traveler)).willReturn(traveler);

            // when
            Traveler result = travelerService.addTraveler(traveler);

            // then
            assertThat(result).isEqualTo(traveler);
            then(travelerRepository).should(times(1)).save(traveler);
        }
    }

    @Nested
    @DisplayName("여행 계획에 속한 여행자 목록 조회(getTravelers) 테스트")
    class GetTravelersTest {
        @Test
        @DisplayName("성공")
        void getTravelersSuccess() {
            // given
            Plan plan = PlanTestBuilder.aPlan().build();
            Traveler traveler1 = TravelerTestBuilder.aTraveler().withId(1L).build();
            Traveler traveler2 = TravelerTestBuilder.aTraveler().withId(2L).build();
            List<Traveler> travelerList = List.of(traveler1, traveler2);

            given(travelerRepository.findByPlanAndStatusOrStatus(plan, InvitationStatus.ACCEPTED, InvitationStatus.INVITED))
                    .willReturn(travelerList);

            // when
            List<TravelerResponse> responses = travelerService.getTravelers(plan);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(1L);
            assertThat(responses.get(1).id()).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("여행자 삭제(deleteTraveler) 테스트")
    class DeleteTravelerTest {
        @Test
        @DisplayName("성공")
        void deleteTravelerSuccess() {
            // given
            Long travelerId = 1L;
            Traveler traveler = TravelerTestBuilder.aTraveler().withId(travelerId).build();
            given(travelerRepository.findById(travelerId)).willReturn(Optional.of(traveler));
            willDoNothing().given(travelerRepository).delete(traveler);

            // when
            travelerService.deleteTraveler(travelerId);

            // then
            then(travelerRepository).should(times(1)).delete(traveler);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 Traveler")
        void deleteTravelerFail_NotFound() {
            // given
            Long nonExistentTravelerId = 999L;
            given(travelerRepository.findById(nonExistentTravelerId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    travelerService.deleteTraveler(nonExistentTravelerId));

            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("TE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 여행자를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("소유주 여부 확인(isOwner) 테스트")
    class IsOwnerTest {
        @Test
        @DisplayName("성공 - 소유주가 맞음")
        void isOwnerReturnsTrue() {
            // given
            Member member = MemberTestBuilder.aMember().build();
            Plan plan = PlanTestBuilder.aPlan().build();
            Traveler ownerTraveler = TravelerTestBuilder.aTraveler()
                    .withMember(member).withPlan(plan).withRole(Role.OWNER).build();
            given(travelerRepository.findByPlanAndMember(plan, member)).willReturn(Optional.of(ownerTraveler));

            // when
            boolean isOwner = travelerService.isOwner(plan, member);

            // then
            assertThat(isOwner).isTrue();
        }

        @Test
        @DisplayName("성공 - 소유주가 아님")
        void isOwnerReturnsFalse() {
            // given
            Member member = MemberTestBuilder.aMember().build();
            Plan plan = PlanTestBuilder.aPlan().build();
            Traveler participantTraveler = TravelerTestBuilder.aTraveler()
                    .withMember(member).withPlan(plan).withRole(Role.PARTICIPANT).build();
            given(travelerRepository.findByPlanAndMember(plan, member)).willReturn(Optional.of(participantTraveler));

            // when
            boolean isOwner = travelerService.isOwner(plan, member);

            // then
            assertThat(isOwner).isFalse();
        }

        @Test
        @DisplayName("실패 - 해당 Plan에 속한 Traveler가 아님")
        void isOwnerFail_TravelerNotFound() {
            // given
            Member member = MemberTestBuilder.aMember().build();
            Plan plan = PlanTestBuilder.aPlan().build();
            given(travelerRepository.findByPlanAndMember(plan, member)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    travelerService.isOwner(plan, member));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("TE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 여행자를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("Traveler 단건 조회(getTraveler) 테스트")
    class GetTravelerTest {

        @Test
        @DisplayName("성공")
        void getTravelerSuccess() {
            // given
            Long travelerId = 1L;
            Traveler traveler = TravelerTestBuilder.aTraveler().withId(travelerId).build();
            given(travelerRepository.findById(travelerId)).willReturn(Optional.of(traveler));

            // when
            Traveler result = travelerService.getTraveler(travelerId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(travelerId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 Traveler")
        void getTravelerFail_NotFound() {
            // given
            Long nonExistentTravelerId = 999L;
            given(travelerRepository.findById(nonExistentTravelerId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    travelerService.getTraveler(nonExistentTravelerId));

            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("TE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 여행자를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("초대 목록 조회(getInvitations) 테스트")
    class GetInvitationsTest {
        @Test
        @DisplayName("성공")
        void getInvitationsSuccess() {
            // given
            Member member = MemberTestBuilder.aMember().build();
            Traveler invitation1 = TravelerTestBuilder.aTraveler().withStatus(InvitationStatus.INVITED).build();
            Traveler invitation2 = TravelerTestBuilder.aTraveler().withStatus(InvitationStatus.INVITED).build();
            given(travelerRepository.findByMemberAndStatus(member, InvitationStatus.INVITED))
                    .willReturn(List.of(invitation1, invitation2));

            // when
            List<Traveler> invitations = travelerService.getInvitations(member);

            // then
            assertThat(invitations).hasSize(2);
            assertThat(invitations).containsExactly(invitation1, invitation2);
        }
    }
}
