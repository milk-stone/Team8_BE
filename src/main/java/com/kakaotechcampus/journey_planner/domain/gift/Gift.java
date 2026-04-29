package com.kakaotechcampus.journey_planner.domain.gift;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "gift",
        uniqueConstraints = @UniqueConstraint(columnNames = {"gift_event_id", "member_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_event_id", nullable = false)
    private GiftEvent giftEvent;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @CreatedDate
    @Column(name = "issued_at", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    public static Gift of(GiftEvent event, Long memberId) {
        Gift gift = new Gift();
        gift.giftEvent = event;
        gift.memberId = memberId;
        return gift;
    }
}
