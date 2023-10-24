package com.thanksd.server.security.auth.google;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleRequest {

    private String content_Type;

    private String grant_type;

    private String client_id;

    private String redirect_uri;

    private String code;

    private String client_secret;
}
