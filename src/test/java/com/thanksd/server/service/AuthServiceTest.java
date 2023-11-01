package com.thanksd.server.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Nation;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.AuthLoginRequest;
import com.thanksd.server.dto.request.KakaoLoginRequest;
import com.thanksd.server.dto.response.OAuthTokenResponse;
import com.thanksd.server.dto.response.TokenResponse;
import com.thanksd.server.exception.badrequest.PasswordMismatchException;
import com.thanksd.server.repository.MemberRepository;
import com.thanksd.server.security.auth.OAuthPlatformMemberResponse;
import com.thanksd.server.security.auth.kakao.KakaoOAuthUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@ServiceTest
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    @MockBean
    private KakaoOAuthUserProvider kakaoOAuthUserProvider;

    @DisplayName("회원 로그인 요청이 옳다면 토큰을 발급한다")
    @Test
    void login() {
        String email = "dlawotn3@naver.com";
        String password = "hihi123";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member("dlawotn3@naver.com", encodedPassword, Platform.THANKSD, null,
                Nation.KOREA);
        memberRepository.save(member);
        AuthLoginRequest loginRequest = new AuthLoginRequest(email, password);

        TokenResponse tokenResponse = authService.login(loginRequest);

        assertNotNull(tokenResponse.getToken());
    }

    @DisplayName("회원 로그인 요청이 올바르지 않다면 예외가 발생한다")
    @Test
    void loginWithException() {
        String email = "dlawotn3@naver.com";
        String password = "hihi123";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member("dlawotn3@naver.com", encodedPassword, Platform.THANKSD, null,
                Nation.KOREA);
        memberRepository.save(member);

        AuthLoginRequest loginRequest = new AuthLoginRequest(email, "wrongPassword");

        assertThrows(PasswordMismatchException.class,
                () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Kakao OAuth 로그인 시 가입되지 않은 회원일 경우 이메일 값을 보내고 isRegistered 값을 false로 보낸다")
    void loginKakaoOAuthNotRegistered() {
        String expected = "dlawotn3@kakao.com";
        String platformId = "1234321";
        when(kakaoOAuthUserProvider.getKakaoPlatformMember(anyString()))
                .thenReturn(new OAuthPlatformMemberResponse(platformId, expected));

        OAuthTokenResponse actual = authService.kakaoOAuthLogin(new KakaoLoginRequest("token"));

        assertAll(
                () -> assertThat(actual.getToken()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected),
                () -> assertThat(actual.getIsRegistered()).isFalse(),
                () -> assertThat(actual.getPlatformId()).isEqualTo(platformId)
        );
    }

    @Test
    @DisplayName("Kakao OAuth 로그인 시 이미 가입된 회원일 경우 토큰과 이메일, 그리고 isRegistered 값을 true로 보낸다")
    void loginKakaoOAuthRegisteredAndMocacongMember() {
        String expected = "dlawotn3@kakao.com";
        String platformId = "1234321";
        Member member = new Member(expected, Platform.KAKAO, platformId, Nation.KOREA);
        memberRepository.save(member);
        when(kakaoOAuthUserProvider.getKakaoPlatformMember(anyString()))
                .thenReturn(new OAuthPlatformMemberResponse(platformId, expected));

        OAuthTokenResponse actual = authService.kakaoOAuthLogin(new KakaoLoginRequest("token"));

        assertAll(
                () -> assertThat(actual.getToken()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected),
                () -> assertThat(actual.getIsRegistered()).isTrue(),
                () -> assertThat(actual.getPlatformId()).isEqualTo(platformId)
        );
    }
}
