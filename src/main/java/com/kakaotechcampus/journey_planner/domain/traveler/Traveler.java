package com.kakaotechcampus.journey_planner.domain.traveler;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "traveler")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Traveler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "traveler_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Traveler createPlan(Member member, Plan plan) {
        Traveler traveler = new Traveler();
        traveler.member = member;
        traveler.plan = plan;
        traveler.status = InvitationStatus.ACCEPTED;
        traveler.role = Role.OWNER;
        return traveler;
    }

    public static Traveler createInvitation(Member member, Plan plan) {
        Traveler traveler = new Traveler();
        traveler.member = member;
        traveler.plan = plan;
        traveler.status = InvitationStatus.INVITED;
        traveler.role = Role.DEFAULT;
        return traveler;
    }

    public void accept() {
        status = InvitationStatus.ACCEPTED;
        role = Role.PARTICIPANT;
    }
}
