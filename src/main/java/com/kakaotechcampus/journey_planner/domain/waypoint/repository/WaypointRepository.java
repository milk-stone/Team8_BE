package com.kakaotechcampus.journey_planner.domain.waypoint.repository;

import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaypointRepository extends JpaRepository<Waypoint, Long> {

    List<Waypoint> findAllByPlanId(Long planId);

    Optional<Waypoint> findByIdAndPlanId(Long waypointId, Long planId);
}
