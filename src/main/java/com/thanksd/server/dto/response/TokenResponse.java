package com.thanksd.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenResponse {
    private Long memberId;
    private String token;

    public static TokenResponse from(final Long memberId, final String token) {
        return new TokenResponse(memberId, token);
    }
}
