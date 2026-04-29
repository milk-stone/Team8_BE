package com.kakaotechcampus.journey_planner.domain.history;

import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeEditHistoryRepository extends JpaRepository<NodeEditHistory, Long> {
    List<NodeEditHistory> findByNodeIdAndNodeSortOrderByEditedAtDesc(Long nodeId, NodeSort nodeSort);

    void deleteByNodeIdAndNodeSort(Long nodeId, NodeSort nodeSort);
}
