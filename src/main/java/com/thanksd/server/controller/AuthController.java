package com.thanksd.server.controller;

import com.thanksd.server.dto.request.AuthLoginRequest;
import com.thanksd.server.dto.response.Response;
import com.thanksd.server.dto.response.TokenResponse;
import com.thanksd.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Login", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "이메일 로그인")
    @PostMapping
    public Response<?> login(@RequestBody @Valid AuthLoginRequest request) {
        TokenResponse response = authService.login(request);
        return Response.ofSuccess("OK", response);
    }
}
