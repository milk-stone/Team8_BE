package com.kakaotechcampus.journey_planner.domain.member.repository;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Member> findAll(Pageable pageable);
}
