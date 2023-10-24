package com.thanksd.server.service;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.AuthLoginRequest;
import com.thanksd.server.dto.response.TokenResponse;
import com.thanksd.server.exception.badrequest.PasswordMismatchException;
import com.thanksd.server.exception.notfound.NotFoundMemberException;
import com.thanksd.server.repository.MemberRepository;
import com.thanksd.server.security.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(AuthLoginRequest request) {
        Member findMember = memberRepository.findByEmailAndPlatform(request.getEmail(), Platform.THANKSD)
                .orElseThrow(NotFoundMemberException::new);
        validatePassword(findMember, request.getPassword());

        String token = issueToken(findMember);
        return TokenResponse.from(findMember.getId(), token);
    }

    private String issueToken(final Member findMember) {
        return jwtTokenProvider.createToken(findMember.getId());
    }

    private void validatePassword(final Member findMember, final String password) {
        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new PasswordMismatchException();
        }
    }
}
