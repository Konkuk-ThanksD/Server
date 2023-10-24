package com.thanksd.server.security.auth.google;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class GoogleResponse {

    @NotNull
    private String token_type;

    @NotNull
    private String access_token;

    @NotBlank
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Integer expires_in;

    @NotNull
    private String refresh_token;

    @NotBlank
    private String scope;
}
