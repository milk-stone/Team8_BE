package com.kakaotechcampus.journey_planner.domain.memo.repository;

import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByIdAndPlanId(Long memoId, Long planId);

    List<Memo> findAllByPlanId(Long planId);
}
