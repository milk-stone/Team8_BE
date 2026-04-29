package com.kakaotechcampus.journey_planner.domain.history;

import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "node_edit_history",
        indexes = @Index(name = "idx_history_node", columnList = "node_id, node_sort"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class NodeEditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    private Long nodeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_sort", nullable = false)
    private NodeSort nodeSort;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @CreatedDate
    @Column(name = "edited_at", nullable = false, updatable = false)
    private LocalDateTime editedAt;

    @Column(name = "snapshot", nullable = false, columnDefinition = "TEXT")
    private String snapshot;

    private NodeEditHistory(Long nodeId, NodeSort nodeSort, Long memberId, String snapshot) {
        this.nodeId = nodeId;
        this.nodeSort = nodeSort;
        this.memberId = memberId;
        this.snapshot = snapshot;
    }

    public static NodeEditHistory of(Long nodeId, NodeSort nodeSort, Long memberId, String snapshot) {
        return new NodeEditHistory(nodeId, nodeSort, memberId, snapshot);
    }
}
