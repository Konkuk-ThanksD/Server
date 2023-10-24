package com.thanksd.server.service;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.AuthLoginRequest;
import com.thanksd.server.dto.response.TokenResponse;
import com.thanksd.server.exception.badrequest.PasswordMismatchException;
import com.thanksd.server.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ServiceTest
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    @DisplayName("회원 로그인 요청이 옳다면 토큰을 발급한다")
    @Test
    void login() {
        String email = "dlawotn3@naver.com";
        String password = "hihi123";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member("dlawotn3@naver.com", encodedPassword, Platform.THANKSD, null);
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
        Member member = new Member("dlawotn3@naver.com", encodedPassword, Platform.THANKSD, null);
        memberRepository.save(member);

        AuthLoginRequest loginRequest = new AuthLoginRequest(email, "wrongPassword");

        assertThrows(PasswordMismatchException.class,
                () -> authService.login(loginRequest));
    }
}
