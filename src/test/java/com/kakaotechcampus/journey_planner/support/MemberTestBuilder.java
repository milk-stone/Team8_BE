package com.kakaotechcampus.journey_planner.support;

import com.kakaotechcampus.journey_planner.domain.member.Member;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class MemberTestBuilder {
    private Long id = 1L;
    private String name = "김철수";
    private String contact = "010-1234-1234";
    private String email = "test@example.com";
    private String password = "encodedPassword";
    private String mbti = "ENTP";

    public static MemberTestBuilder aMember() {
        return new MemberTestBuilder();
    }

    public MemberTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MemberTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MemberTestBuilder withContact(String contact) {
        this.contact = contact;
        return this;
    }

    public MemberTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public MemberTestBuilder withMbti(String mbti) {
        this.mbti = mbti;
        return this;
    }

    public Member build() {
        Member member = new Member(this.name, this.contact, this.email, this.password, this.mbti);

        // 리플렉션을 사용해 private인 id 필드에 값을 강제로 주입
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, this.id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("테스트 객체 생성 중 id 필드 설정 실패", e);
        }

        return member;
    }
}
