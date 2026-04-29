package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.application.waypoint.WaypointService;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import com.kakaotechcampus.journey_planner.domain.waypoint.repository.WaypointRepository;
import com.kakaotechcampus.journey_planner.domain.waypoint.LocationCategory;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.request.WaypointRequest;
import com.kakaotechcampus.journey_planner.presentation.waypoint.dto.response.WaypointResponse;
import com.kakaotechcampus.journey_planner.support.PlanTestBuilder;
import com.kakaotechcampus.journey_planner.support.WaypointTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class WaypointServiceTest {
    @InjectMocks
    private WaypointService waypointService;

    @Mock
    private PlanService planService;
    @Mock
    private WaypointRepository waypointRepository;

    @Nested
    @DisplayName("경유지 생성(createWaypoint) 테스트")
    class CreateWaypointTest {
        @Test
        @DisplayName("성공")
        void createWaypointSuccess() {
            // given
            Long planId = 1L;
            WaypointRequest request = new WaypointRequest("경유지", "설명", "주소",
                    LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                    LocationCategory.HOTEL, 1.0f, 2.0f);

            Plan mockPlan = mock(Plan.class);
            Waypoint savedWaypoint = WaypointTestBuilder.aWaypoint().withId(1L).build();

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(waypointRepository.save(any(Waypoint.class))).willReturn(savedWaypoint);

            // when
            WaypointResponse response = waypointService.createWaypoint(planId, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            then(mockPlan).should(times(1)).addWaypoint(any(Waypoint.class));
        }
    }

    @Nested
    @DisplayName("경유지 수정(updateWaypoint) 테스트")
    class UpdateWaypointTest {
        @Test
        @DisplayName("성공")
        void updateWaypointSuccess() {
            // given
            Long planId = 1L;
            Long waypointId = 1L;
            WaypointRequest request = new WaypointRequest("수정된 이름", "수정된 설명", "수정된 주소",
                    null, null, LocationCategory.HOTEL, 3.0f, 4.0f);

            Waypoint mockWaypoint = mock(Waypoint.class);
            given(waypointRepository.findByIdAndPlanId(waypointId, planId)).willReturn(Optional.of(mockWaypoint));

            // when
            waypointService.updateWaypoint(planId, waypointId, request);

            // then
            then(mockWaypoint).should(times(1)).update(
                    request.name(), request.description(), request.address(),
                    request.startTime(), request.endTime(), request.locationCategory(),
                    request.xPosition(), request.yPosition()
            );
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 경유지")
        void updateWaypointFail_NotFound() {
            // given
            Long planId = 1L;
            Long waypointId = 999L;
            WaypointRequest request = new WaypointRequest("이름", null, null, null, null, null, null, null);
            given(waypointRepository.findByIdAndPlanId(waypointId, planId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    waypointService.updateWaypoint(planId, waypointId, request));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("WE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 웨이포인트를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("경유지 삭제(deleteWaypoint) 테스트")
    class DeleteWaypointTest {
        @Test
        @DisplayName("성공")
        void deleteWaypointSuccess() {
            // given
            Long planId = 1L;
            Long waypointId = 1L;
            Plan mockPlan = mock(Plan.class);
            Waypoint mockWaypoint = mock(Waypoint.class);

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(waypointRepository.findByIdAndPlanId(waypointId, planId)).willReturn(Optional.of(mockWaypoint));

            // when
            waypointService.deleteWaypoint(planId, waypointId);

            // then
            then(mockPlan).should(times(1)).removeWaypoint(mockWaypoint);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 경유지")
        void deleteWaypointFail_NotFound() {
            // given
            Long planId = 1L;
            Long waypointId = 999L;
            Plan plan = PlanTestBuilder.aPlan().build();

            given(planService.getPlanEntity(planId)).willReturn(plan);
            given(waypointRepository.findByIdAndPlanId(waypointId, planId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    waypointService.deleteWaypoint(planId, waypointId));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("WE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 웨이포인트를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("경유지 목록 조회(getWaypoints) 테스트")
    class GetWaypointsTest {
        @Test
        @DisplayName("성공")
        void getWaypointsSuccess() {
            // given
            Long planId = 1L;
            Plan plan = PlanTestBuilder.aPlan().withId(planId).build();
            Waypoint waypoint1 = WaypointTestBuilder.aWaypoint().withId(101L).build();
            Waypoint waypoint2 = WaypointTestBuilder.aWaypoint().withId(102L).build();

            given(planService.getPlanEntity(planId)).willReturn(plan);
            given(waypointRepository.findAllByPlanId(plan.getId())).willReturn(List.of(waypoint1, waypoint2));

            // when
            List<WaypointResponse> responses = waypointService.getWaypoints(planId);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(101L);
        }
    }

    @Nested
    @DisplayName("Waypoint 엔티티 조회(getWaypointEntity) 테스트")
    class GetWaypointEntityTest {
        @Test
        @DisplayName("성공")
        void getWaypointEntitySuccess() {
            // given
            Long waypointId = 1L;
            Waypoint waypoint = WaypointTestBuilder.aWaypoint().withId(waypointId).build();
            given(waypointRepository.findById(waypointId)).willReturn(Optional.of(waypoint));

            // when
            Waypoint result = waypointService.getWaypointEntity(waypointId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(waypointId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 경유지")
        void getWaypointEntityFail_NotFound() {
            // given
            Long waypointId = 999L;
            given(waypointRepository.findById(waypointId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    waypointService.getWaypointEntity(waypointId));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("WE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 웨이포인트를 찾을 수 없습니다.");
        }
    }
}
