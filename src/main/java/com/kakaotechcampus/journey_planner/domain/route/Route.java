package com.kakaotechcampus.journey_planner.domain.route;

import com.kakaotechcampus.journey_planner.domain.node.Node;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.waypoint.Waypoint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "route")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route extends Node {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_waypoint_id", nullable = false)
    private Waypoint fromWayPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_waypoint_id", nullable = false)
    private Waypoint toWayPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleCategory vehicleCategory;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Float durationMin;


    public Route(Plan plan,
                 Waypoint fromWayPoint,
                 Waypoint toWayPoint,
                 String title,
                 String description,
                 Float duration,
                 VehicleCategory vehicleCategory) {
        super(NodeSort.ROUTE);
        this.plan = plan;
        this.fromWayPoint = fromWayPoint;
        this.toWayPoint = toWayPoint;
        this.title = title;
        this.description = description;
        this.durationMin = duration;
        this.vehicleCategory = vehicleCategory;
    }

    public void update(Waypoint from, Waypoint to,
                       String title,
                       String description,
                       Float duration,
                       VehicleCategory vehicleCategory) {
        this.fromWayPoint = from;
        this.toWayPoint = to;
        this.title = title;
        this.description = description;
        this.durationMin = duration;
        this.vehicleCategory = vehicleCategory;
    }

    public void assignToPlan(Plan plan) {
        this.plan = plan;
    }

    // ✅ 연관관계 편의 메서드
    public void setFromWayPoint(Waypoint fromWayPoint) {
        this.fromWayPoint = fromWayPoint;
    }

    public void setToWayPoint(Waypoint toWayPoint) {
        this.toWayPoint = toWayPoint;
    }
}
