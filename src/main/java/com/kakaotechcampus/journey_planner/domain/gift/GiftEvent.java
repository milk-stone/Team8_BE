package com.kakaotechcampus.journey_planner.domain.gift;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.global.common.auditing.BaseEntity;
import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gift_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GiftEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private Long createdBy;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false)
    private int remainingCount;

    @Column(nullable = false)
    private String content;

    public static GiftEvent of(Plan plan, Long createdBy, int totalCount, String content) {
        GiftEvent event = new GiftEvent();
        event.plan = plan;
        event.createdBy = createdBy;
        event.totalCount = totalCount;
        event.remainingCount = totalCount;
        event.content = content;
        return event;
    }

    public void decreaseRemaining() {
        if (remainingCount <= 0) {
            throw new BusinessException(ErrorCode.GIFT_SOLD_OUT);
        }
        this.remainingCount--;
    }
}
