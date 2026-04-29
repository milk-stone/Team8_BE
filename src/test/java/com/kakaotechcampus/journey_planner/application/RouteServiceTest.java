package com.kakaotechcampus.journey_planner.application;

import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.application.route.RouteService;
import com.kakaotechcampus.journey_planner.application.waypoint.WaypointService;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.route.VehicleCategory;
import com.kakaotechcampus.journey_planner.domain.route.repository.RouteRepository;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.presentation.route.dto.request.RouteRequest;
import com.kakaotechcampus.journey_planner.presentation.route.dto.response.RouteResponse;
import com.kakaotechcampus.journey_planner.support.PlanTestBuilder;
import com.kakaotechcampus.journey_planner.support.RouteTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
public class RouteServiceTest {
    @InjectMocks
    private RouteService routeService;

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private PlanService planService;
    @Mock
    private WaypointService waypointService;

    @Nested
    @DisplayName("경로 생성(createRoute) 테스트")
    class CreateRouteTest {
        @Test
        @DisplayName("성공")
        void createRouteSuccess() {
            // given
            Long planId = 1L;
            RouteRequest request = new RouteRequest(101L, 102L, "강남역에서 서울역", "지하철 이용", 30.0f, VehicleCategory.TRAIN);

            Plan mockPlan = mock(Plan.class);
            Waypoint mockFromWaypoint = mock(Waypoint.class);
            Waypoint mockToWaypoint = mock(Waypoint.class);

            Route savedRoute = RouteTestBuilder.aRoute()
                    .withId(1L)
                    .withFromWayPoint(mockFromWaypoint)
                    .withToWayPoint(mockToWaypoint)
                    .withTitle("강남역에서 서울역")
                    .withDescription("지하철 이용")
                    .withDurationMin(30.0f)
                    .withVehicleCategory(VehicleCategory.TRAIN)
                    .build();

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(waypointService.getWaypointEntity(request.fromWaypointId())).willReturn(mockFromWaypoint);
            given(waypointService.getWaypointEntity(request.toWaypointId())).willReturn(mockToWaypoint);

            given(mockFromWaypoint.getPlan()).willReturn(mockPlan);
            given(mockToWaypoint.getPlan()).willReturn(mockPlan);
            given(mockPlan.getId()).willReturn(planId);

            given(routeRepository.save(any(Route.class))).willReturn(savedRoute);

            // when
            RouteResponse response = routeService.createRoute(planId, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("강남역에서 서울역");

            then(mockPlan).should(times(1)).addRoute(any(Route.class));
        }
    }

    @Nested
    @DisplayName("경로 수정(updateRoute) 테스트")
    class UpdateRouteTest {
        @Test
        @DisplayName("성공")
        void updateRouteSuccess() {
            // given
            Long planId = 1L;
            Long routeId = 1L;
            RouteRequest request = new RouteRequest(103L, 104L, "수정된 제목", "수정된 설명", 45.0f, VehicleCategory.BUS);

            Plan mockPlan = mock(Plan.class);
            Route mockRoute = mock(Route.class);

            Waypoint mockFromWaypoint = mock(Waypoint.class);
            Waypoint mockToWaypoint = mock(Waypoint.class);

            given(routeRepository.findByIdAndPlanId(routeId, planId)).willReturn(Optional.of(mockRoute));
            given(waypointService.getWaypointEntity(request.fromWaypointId())).willReturn(mockFromWaypoint);
            given(waypointService.getWaypointEntity(request.toWaypointId())).willReturn(mockToWaypoint);

            given(mockRoute.getId()).willReturn(routeId);
            given(mockRoute.getFromWayPoint()).willReturn(mockFromWaypoint);
            given(mockRoute.getToWayPoint()).willReturn(mockToWaypoint);
            given(mockFromWaypoint.getId()).willReturn(request.fromWaypointId());
            given(mockToWaypoint.getId()).willReturn(request.toWaypointId());
            given(mockRoute.getTitle()).willReturn("수정된 제목");

            // when
            RouteResponse response = routeService.updateRoute(planId, routeId, request);

            // then
            assertThat(response.id()).isEqualTo(routeId);
            assertThat(response.title()).isEqualTo("수정된 제목");

            then(mockRoute).should(times(1)).update(
                    mockFromWaypoint, mockToWaypoint, "수정된 제목", "수정된 설명", 45.0f, VehicleCategory.BUS
            );
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 경로")
        void updateRouteFail_RouteNotFound() {
            // given
            Long planId = 1L;
            Long routeId = 999L;
            RouteRequest request = new RouteRequest(101L, 102L, "제목", "설명", 30.0f, VehicleCategory.CAR);
            given(routeRepository.findByIdAndPlanId(routeId, planId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    routeService.updateRoute(planId, routeId, request));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("RE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 경로를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("경로 삭제(deleteRoute) 테스트")
    class DeleteRouteTest {
        @Test
        @DisplayName("성공")
        void deleteRouteSuccess() {
            // given
            Long planId = 1L;
            Long routeId = 1L;
            Plan mockPlan = mock(Plan.class);
            Route mockRoute = mock(Route.class);

            given(planService.getPlanEntity(planId)).willReturn(mockPlan);
            given(routeRepository.findByIdAndPlanId(routeId, planId)).willReturn(Optional.of(mockRoute));

            // when
            routeService.deleteRoute(planId, routeId);

            // then
            then(mockPlan).should(times(1)).removeRoute(mockRoute);
        }
    }

    @Nested
    @DisplayName("전체 경로 목록 조회(getRoutes) 테스트")
    class GetRoutesTest {
        @Test
        @DisplayName("성공")
        void getRoutesSuccess() {
            // given
            Long planId = 1L;
            Plan plan = PlanTestBuilder.aPlan().withId(planId).build();
            Route route1 = RouteTestBuilder.aRoute().withId(101L).build();
            Route route2 = RouteTestBuilder.aRoute().withId(102L).build();

            given(planService.getPlanEntity(planId)).willReturn(plan);
            given(routeRepository.findAllByPlanId(plan.getId())).willReturn(List.of(route1, route2));

            // when
            List<RouteResponse> responses = routeService.getRoutes(planId);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(101L);
            assertThat(responses.get(1).id()).isEqualTo(102L);
        }
    }

    @Nested
    @DisplayName("Route 엔티티 조회(getRouteEntity) 테스트")
    class GetRouteEntityTest {
        @Test
        @DisplayName("성공")
        void getRouteEntitySuccess() {
            // given
            Long routeId = 1L;
            Route route = RouteTestBuilder.aRoute().withId(routeId).build();
            given(routeRepository.findById(routeId)).willReturn(Optional.of(route));

            // when
            Route result = routeService.getRouteEntity(routeId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(routeId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 경로")
        void getRouteEntityFail_RouteNotFound() {
            // given
            Long routeId = 999L;
            given(routeRepository.findById(routeId)).willReturn(Optional.empty());

            // when & then
            BusinessException exception = assertThrows(BusinessException.class, () ->
                    routeService.getRouteEntity(routeId));
            assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(exception.getCode()).isEqualTo("RE_001");
            assertThat(exception.getMessage()).isEqualTo("해당 경로를 찾을 수 없습니다.");
        }
    }
}
