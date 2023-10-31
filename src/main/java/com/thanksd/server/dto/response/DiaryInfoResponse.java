package com.thanksd.server.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DiaryInfoResponse {
    private Long id;
    private String image;
}
