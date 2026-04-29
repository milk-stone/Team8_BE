package com.kakaotechcampus.journey_planner.domain.plan;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import com.kakaotechcampus.journey_planner.domain.memo.Memo;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.presentation.plan.dto.request.UpdatePlanRequest;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    private String title;

    private String description;
    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수 값입니다.")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Traveler> travelers = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Waypoint> waypoints = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Route> routes = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Memo> memos = new ArrayList<>();


    public Plan(String title, String description, LocalDate startDate, LocalDate endDate, Member member) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }

    public void update(UpdatePlanRequest request) {
        if (request.title() != null) {
            this.title = request.title();
        }
        if (request.description() != null) {
            this.description = request.description();
        }
        if (request.startDate() != null) {
            this.startDate = request.startDate();
        }
        if (request.endDate() != null) {
            this.endDate = request.endDate();
        }
    }

    public void addTraveler(Traveler traveler) {
        travelers.add(traveler);
    }

    public boolean hasMember(Member member) {
        return this.travelers.stream()
                .map(Traveler::getMember)
                .anyMatch(planMember -> planMember.equals(member));
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        waypoint.assignToPlan(this);
    }

    public void removeWaypoint(Waypoint waypoint) {
        waypoints.remove(waypoint);
        waypoint.assignToPlan(null);
    }

    public void addRoute(Route route) {
        routes.add(route);
        route.assignToPlan(this);
    }

    public void removeRoute(Route route) {
        routes.remove(route);
        route.assignToPlan(null);
    }

    public void addMemo(Memo memo) {
        memos.add(memo);
        memo.assignToPlan(this);
    }

    public void removeMemo(Memo memo) {
        memos.remove(memo);
        memo.assignToPlan(null);
    }

}
