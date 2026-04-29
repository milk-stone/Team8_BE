package com.kakaotechcampus.journey_planner.application.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotechcampus.journey_planner.domain.history.NodeEditHistory;
import com.kakaotechcampus.journey_planner.domain.history.NodeEditHistoryRepository;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import com.kakaotechcampus.journey_planner.presentation.history.dto.NodeEditHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NodeEditHistoryService {

    private final NodeEditHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void record(Long nodeId, NodeSort nodeSort, Long memberId, Object responseDto) {
        try {
            String snapshot = objectMapper.writeValueAsString(responseDto);
            historyRepository.save(NodeEditHistory.of(nodeId, nodeSort, memberId, snapshot));
        } catch (JsonProcessingException e) {
            log.error("[History] snapshot 직렬화 실패 nodeId={} nodeSort={}: {}", nodeId, nodeSort, e.getMessage());
        }
    }

    @Transactional
    public void deleteHistory(Long nodeId, NodeSort nodeSort) {
        historyRepository.deleteByNodeIdAndNodeSort(nodeId, nodeSort);
    }

    @Transactional(readOnly = true)
    public List<NodeEditHistoryResponse> getHistory(Long nodeId, NodeSort nodeSort) {
        return historyRepository.findByNodeIdAndNodeSortOrderByEditedAtDesc(nodeId, nodeSort)
                .stream()
                .map(NodeEditHistoryResponse::from)
                .toList();
    }
}
