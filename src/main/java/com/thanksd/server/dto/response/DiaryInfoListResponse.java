package com.thanksd.server.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DiaryInfoListResponse {
    private List<DiaryInfoResponse> diaryList;
}
