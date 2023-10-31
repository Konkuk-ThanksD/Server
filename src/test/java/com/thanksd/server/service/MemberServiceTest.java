package com.thanksd.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Nation;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.MemberSignUpRequest;
import com.thanksd.server.dto.request.OAuthMemberSignUpRequest;
import com.thanksd.server.exception.badrequest.DuplicateMemberException;
import com.thanksd.server.exception.notfound.NotFoundMemberException;
import com.thanksd.server.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        memberRepository.save(new Member(email, Platform.THANKSD, "1111", Nation.KOREA));
        MemberSignUpRequest request = new MemberSignUpRequest(email, "a1b2c3d4");

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(DuplicateMemberException.class);
    }

    @Test
    @DisplayName("OAuth 유저 로그인 후 정보를 입력받아 회원을 가입한다")
    void signUpByOAuthMember() {
        String email = "dlawotn3@naver.com";
        String platformId = "1234321";
        Member savedMember = memberRepository.save(new Member(email, Platform.KAKAO, platformId, Nation.KOREA));
        OAuthMemberSignUpRequest request = new OAuthMemberSignUpRequest(null, Platform.KAKAO.getValue(),
                platformId, Nation.KOREA.getValue());

        memberService.signUpByOAuthMember(request);

        Member actual = memberRepository.findById(savedMember.getId()).orElseThrow();
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("OAuth 유저 로그인 후 회원가입 시 platform과 platformId 정보로 회원이 존재하지 않으면 예외를 반환한다")
    void signUpByOAuthMemberWhenInvalidPlatformInfo() {
        memberRepository.save(new Member("dlawotn3@naver.com", Platform.KAKAO, "1234321", Nation.KOREA));
        OAuthMemberSignUpRequest request = new OAuthMemberSignUpRequest(null, Platform.KAKAO.getValue(),
                "invalid", Nation.KOREA.getValue());

        assertThatThrownBy(() -> memberService.signUpByOAuthMember(request))
                .isInstanceOf(NotFoundMemberException.class);
    }
}
