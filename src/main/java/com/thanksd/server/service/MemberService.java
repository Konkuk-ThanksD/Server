package com.thanksd.server.service;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import com.thanksd.server.dto.request.MemberSignUpRequest;
import com.thanksd.server.dto.response.MemberSignUpResponse;
import com.thanksd.server.exception.badrequest.DuplicateMemberException;
import com.thanksd.server.exception.badrequest.InvalidEmailException;
import com.thanksd.server.exception.badrequest.InvalidPasswordException;
import com.thanksd.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,20}$");

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberSignUpResponse signUp(MemberSignUpRequest request) {
        validatePassword(request.getPassword());
        validateDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        try {
            Member member = new Member(request.getEmail(), encodedPassword);
            return new MemberSignUpResponse(memberRepository.save(member).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateMemberException();
        }
    }

    private void validateDuplicateMember(MemberSignUpRequest memberSignUpRequest) {
        if (memberRepository.existsByEmailAndPlatform(memberSignUpRequest.getEmail(), Platform.THANKSD)) {
            throw new DuplicateMemberException();
        }
    }

    private void validatePassword(String password) {
        if (!PASSWORD_REGEX.matcher(password).matches()) {
            throw new InvalidPasswordException();
        }
    }
}
