package com.kakaotechcampus.journey_planner.domain.member;

import com.kakaotechcampus.journey_planner.domain.traveler.Traveler;
import com.kakaotechcampus.journey_planner.global.common.auditing.BaseEntity;
import com.kakaotechcampus.journey_planner.presentation.member.dto.request.ModifyMemberRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String contact;

    private String email;

    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    private MbtiType mbtiType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Traveler> travelers = new ArrayList<>();

    public Member(String name, String contact, String email, String password, String mbti) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.mbtiType = MbtiType.valueOf(mbti.toUpperCase());
    }

    public void modify(ModifyMemberRequest response) {
        if (response.username() != null && !response.username().isEmpty()) {
            this.name = response.username();
        }
        if (response.contact() != null && !response.contact().isEmpty()) {
            this.contact = response.contact();
        }
        if (response.email() != null && !response.email().isEmpty()) {
            this.email = response.email();
        }
        if (response.mbti() != null) {
            this.mbtiType = response.mbti();
        }
    }

    public boolean verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void addTraveler(Traveler traveler) {
        travelers.add(traveler);
    }
}

