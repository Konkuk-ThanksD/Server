package com.thanksd.server.security.auth.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "google-info-client", url = "https://www.googleapis.com/oauth2/v1/userinfo")
public interface GoogleInfoClient {

    @GetMapping("?access_token={TOKEN}")
    GoogleInfoResponse getUserInfo(@PathVariable("TOKEN") String token);
}
