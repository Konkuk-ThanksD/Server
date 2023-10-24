package com.thanksd.server.security.auth.google;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "google-auth-client", url = "https://oauth2.googleapis.com/token")
public interface GoogleAuthClient {

    @PostMapping()
    GoogleTokenResponse getGoogleToken(GoogleTokenRequest request);
}
