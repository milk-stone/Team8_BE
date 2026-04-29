package com.kakaotechcampus.journey_planner.domain.route.repository;

import com.kakaotechcampus.journey_planner.domain.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findAllByPlanId(Long planId);

    Optional<Route> findByIdAndPlanId(Long id, Long planId);
}
