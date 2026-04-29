package com.kakaotechcampus.journey_planner.domain.plan.repository;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("SELECT t.plan " +
            "FROM Traveler t " +
            "WHERE t.member.id = :memberId " +
            "AND " +
            "t.status = 'ACCEPTED' " +
            "ORDER BY t.id")
    List<Plan> findAllByMemberId(@Param("memberId") Long memberId);
}
