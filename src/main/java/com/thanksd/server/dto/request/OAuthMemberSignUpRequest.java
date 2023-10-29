package com.thanksd.server.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class OAuthMemberSignUpRequest {

    private String email;

    @NotBlank(message = "1005:공백일 수 없습니다.")
    private String platform;

    @NotBlank(message = "1005:공백일 수 없습니다.")
    private String platformId;

    @NotBlank(message = "1005:공백일 수 없습니다.")
    private String nation;
}
