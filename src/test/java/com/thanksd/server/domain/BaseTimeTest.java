package com.thanksd.server.domain;

import com.thanksd.server.repository.MemberRepository;
import com.thanksd.server.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class BaseTimeTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("멤버를 저장하면 생성 시각이 자동으로 저장된다")
    public void memberCreatedAtNow() {
        Member member = new Member("dlawotn3@naver.com", Platform.KAKAO, "1111");

        memberRepository.save(member);

        assertThat(member.getCreatedTime()).isNotNull();
    }
}
