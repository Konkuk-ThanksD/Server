package com.thanksd.server.controller;

import com.thanksd.server.dto.request.MemberSignUpRequest;
import com.thanksd.server.dto.request.OAuthMemberSignUpRequest;
import com.thanksd.server.dto.response.MemberSignUpResponse;
import com.thanksd.server.dto.response.OAuthMemberSignUpResponse;
import com.thanksd.server.dto.response.Response;
import com.thanksd.server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Members", description = "회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "이메일 회원가입")
    @PostMapping
    public Response<?> signUp(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpResponse response = memberService.signUp(request);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "OAuth 회원가입")
    @PostMapping("/oauth")
    public Response<Object> signUp(@RequestBody @Valid OAuthMemberSignUpRequest request) {
        OAuthMemberSignUpResponse response = memberService.signUpByOAuthMember(request);
        return Response.ofSuccess("OK", response);
    }
}
