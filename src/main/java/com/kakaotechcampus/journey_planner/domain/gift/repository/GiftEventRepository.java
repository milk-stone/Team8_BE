package com.kakaotechcampus.journey_planner.domain.gift.repository;

import com.kakaotechcampus.journey_planner.domain.gift.GiftEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftEventRepository extends JpaRepository<GiftEvent, Long> {
    Optional<GiftEvent> findByIdAndPlanId(Long eventId, Long planId);
}
