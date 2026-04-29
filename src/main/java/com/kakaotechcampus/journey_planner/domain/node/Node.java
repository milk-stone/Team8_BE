package com.kakaotechcampus.journey_planner.domain.node;

import com.kakaotechcampus.journey_planner.global.common.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class Node extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "plan_id", insertable = false, updatable = false)
    private Long planId;

    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeStatus nodeStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeSort nodeSort;

    protected Node(NodeSort nodeSort) {
        this.uuid = UUID.randomUUID().toString();
        this.nodeStatus = NodeStatus.UNLOCK;
        this.nodeSort = nodeSort;
    }

    protected Node() {
    }

    public String getLockKey() {
        return "lock:" + nodeSort.name() + ":" + id;
    }
}
