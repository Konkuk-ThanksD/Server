package com.thanksd.server.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DiaryDateResponse {
    private List<Date> dateList;
}
