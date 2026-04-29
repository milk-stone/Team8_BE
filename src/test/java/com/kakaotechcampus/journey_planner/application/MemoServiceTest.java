package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.memo.MemoService;
import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.application.route.RouteService;
import com.kakaotechcampus.journey_planner.application.waypoint.WaypointService;
import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import com.kakaotechcampus.journey_planner.domain.memo.repository.MemoRepository;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.request.MemoRequest;
import com.kakaotechcampus.journey_planner.presentation.memo.dto.response.MemoResponse;
import com.kakaotechcampus.journey_planner.support.MemoTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemoServiceTest {
    @InjectMocks
    private MemoService memoService;

    @Mock
    private MemoRepository memoRepository;
    @Mock
    private PlanService planService;
    @Mock
    private WaypointService waypointService;
    @Mock
    private RouteService routeService;

    @Nested
    @DisplayName("메모 생성(createMemo) 테스트")
    class CreateMemoTest {

        @Test
        @DisplayName("성공 - Waypoint에 메모 할당")
        void createMemoForWaypointSuccess() {
            // given
            Long planId = 1L;
            Long waypointId = 101L;
            MemoRequest request = new MemoRequest("제목", "내용", 1.0f, 2.0f, waypointId, null);
            Plan mockPlan = mock(Plan.class);
            Waypoint mockWaypoint = mock(Waypoint.class);
            Memo savedMemo = MemoTestBuilder.aMemo()
                    .withId(1L)
                    .withTitle("제목")
                    .withContent("내용")
                    .withXPosition(1.0f)
                    .withYPosition(2.0f)
                    .withPlan(mockPlan)
                    .withWaypoint(mockWaypoint)
                    .build();

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(waypointService.getWaypointEntity(waypointId)).willReturn(mockWaypoint);
            given(memoRepository.save(any(Memo.class))).willReturn(savedMemo);

            // when
            MemoResponse response = memoService.createMemo(planId, request);

            // then
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("제목");

            ArgumentCaptor<Memo> memoCaptor = ArgumentCaptor.forClass(Memo.class);
            then(mockPlan).should(times(1)).addMemo(memoCaptor.capture());
            assertThat(memoCaptor.getValue().getWaypoint()).isEqualTo(mockWaypoint);
        }

        @Test
        @DisplayName("성공 - Route에 메모 할당")
        void createMemoForRouteSuccess() {
            // given
            Long planId = 1L;
            Long routeId = 201L;
            MemoRequest request = new MemoRequest("제목", "내용", 1.0f, 2.0f, null, routeId);
            Plan mockPlan = mock(Plan.class);
            Route mockRoute = mock(Route.class);
            Memo savedMemo = MemoTestBuilder.aMemo()
                    .withId(1L)
                    .withTitle("제목")
                    .withContent("내용")
                    .withXPosition(1.0f)
                    .withYPosition(2.0f)
                    .withRoute(mockRoute).build();

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(routeService.getRouteEntity(routeId)).willReturn(mockRoute);
            given(memoRepository.save(any(Memo.class))).willReturn(savedMemo);

            // when
            MemoResponse response = memoService.createMemo(planId, request);

            // then
            assertThat(response.id()).isEqualTo(1L);
            ArgumentCaptor<Memo> memoCaptor = ArgumentCaptor.forClass(Memo.class);
            then(mockPlan).should(times(1)).addMemo(memoCaptor.capture());
            assertThat(memoCaptor.getValue().getRoute()).isEqualTo(mockRoute);
        }
    }

    @Nested
    @DisplayName("메모 수정(updateMemo) 테스트")
    class UpdateMemoTest {

        @Test
        @DisplayName("성공")
        void updateMemoSuccess() {
            // given
            Long planId = 1L;
            Long memoId = 1L;
            MemoRequest request = new MemoRequest("수정된 제목", "수정된 내용", 3.0f, 4.0f, null, null);
            Memo existingMemo = mock(Memo.class);
            given(memoRepository.findByIdAndPlanId(memoId, planId)).willReturn(Optional.of(existingMemo));

            // when
            MemoResponse response = memoService.updateMemo(planId, memoId, request);

            // then
            then(existingMemo).should(times(1)).update(
                    "수정된 제목", "수정된 내용", 3.0f, 4.0f
            );
            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 메모")
        void updateMemoFail_MemoNotFound() {
            // given
            Long planId = 1L;
            Long memoId = 999L;
            MemoRequest request = new MemoRequest("제목", "내용", 1.0f, 2.0f, null, null);
            given(memoRepository.findByIdAndPlanId(memoId, planId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    memoService.updateMemo(planId, memoId, request)
            );
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("MEE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 메모를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("메모 삭제(deleteMemo) 테스트")
    class DeleteMemoTest {

        @Test
        @DisplayName("성공")
        void deleteMemoSuccess() {
            // given
            Long planId = 1L;
            Long memoId = 1L;
            Plan mockPlan = mock(Plan.class);
            Memo mockMemo = mock(Memo.class);

            given(memoRepository.findByIdAndPlanId(memoId, planId)).willReturn(Optional.of(mockMemo));
            given(mockMemo.getPlan()).willReturn(mockPlan);

            // when
            memoService.deleteMemo(planId, memoId);

            // then
            then(mockPlan).should(times(1)).removeMemo(mockMemo);
        }
    }
}
