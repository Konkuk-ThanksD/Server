package com.thanksd.server.dto.response;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DiaryWeekCountResponse {
//    int monday;
//    int tuesday;
//    int wednesday;
//    int thursday;
//    int friday;
//    int saturday;
//    int sunday;
    Map<String,Integer> weekCounts;
}
