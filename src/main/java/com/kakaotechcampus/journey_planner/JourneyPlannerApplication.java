package com.kakaotechcampus.journey_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JourneyPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JourneyPlannerApplication.class, args);
    }

}
