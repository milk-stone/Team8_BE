package com.kakaotechcampus.journey_planner.domain.waypoint;

import com.kakaotechcampus.journey_planner.domain.node.Node;
import com.kakaotechcampus.journey_planner.domain.node.NodeSort;
import com.kakaotechcampus.journey_planner.domain.plan.Plan;
import com.kakaotechcampus.journey_planner.domain.route.Route;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waypoint extends Node {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    private String name;

    private String description;

    private String address;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationCategory locationCategory;

    private Float xPosition;
    private Float yPosition;

    // fromWayPoint로 연결된 Route들
    @OneToMany(mappedBy = "fromWayPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routesFrom = new ArrayList<>();

    // toWayPoint로 연결된 Route들
    @OneToMany(mappedBy = "toWayPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routesTo = new ArrayList<>();


    public Waypoint(String name, String description, String address, LocalDateTime startTime, LocalDateTime endTime, LocationCategory locationCategory, Float xPosition, Float yPosition) {
        super(NodeSort.WAYPOINT);
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationCategory = locationCategory;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void assignToPlan(Plan plan) {
        this.plan = plan;
    }

    public void update(String name, String description, String address, LocalDateTime startTime, LocalDateTime endTime, LocationCategory locationCategory, Float xPosition, Float yPosition) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationCategory = locationCategory;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
