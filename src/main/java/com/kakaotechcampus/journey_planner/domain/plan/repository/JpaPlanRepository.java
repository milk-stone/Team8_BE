package com.kakaotechcampus.journey_planner.domain.plan.repository;

import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.plan.QPlan;
import com.kakaotechcampus.journey_planner.domain.traveler.InvitationStatus;
import com.kakaotechcampus.journey_planner.domain.traveler.QTraveler;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPlanRepository {
    private final JPAQueryFactory queryFactory;

    public Slice<Plan> findAllByMemberId(Long memberId, Pageable pageable) {
        QPlan qPlan = QPlan.plan;
        QTraveler qTraveler = QTraveler.traveler;

        int pageSize = pageable.getPageSize();

        List<Plan> results = queryFactory
                // distinct() 추가: 내가 소유자이면서 초대도 된 경우, 중복 조회를 방지
                .select(qPlan).distinct()
                .from(qPlan)
                .join(qPlan.travelers, qTraveler)
                .where(
                        // 2. [변경] 내가 소유자인 경우
                        qPlan.member.id.eq(memberId)
                                .or(
                                        qTraveler.member.id.eq(memberId)
                                                .and(qTraveler.status.eq(InvitationStatus.ACCEPTED))
                                )
                )
                .orderBy(qPlan.id.desc())
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;

        if (results.size() > pageSize) {
            results.remove(pageSize);
            hasNext = true;
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }
}
