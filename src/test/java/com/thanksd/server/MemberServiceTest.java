package com.thanksd.server;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.MemberSignUpRequest;
import com.thanksd.server.exception.badrequest.DuplicateMemberException;
import com.thanksd.server.repository.MemberRepository;
import com.thanksd.server.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원을 정상적으로 가입한다")
    void signUp() {
        String expected = "dlawotn3@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest(expected, "a1b2c3d4");

        memberService.signUp(request);

        List<Member> actual = memberRepository.findAll();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getEmail()).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 가입된 이메일이 존재하면 회원 가입 시에 예외를 반환한다")
    void signUpByDuplicateEmailMember() {
        String email = "dlawotn3@naver.com";
        memberRepository.save(new Member(email, Platform.THANKSD, "1111"));
        MemberSignUpRequest request = new MemberSignUpRequest(email, "a1b2c3d4");

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(DuplicateMemberException.class);
    }
}
