package com.thanksd.server.service;

import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.request.DiaryUpdateRequest;
import com.thanksd.server.dto.response.DiaryAllResponse;
import com.thanksd.server.dto.response.DiaryDateResponse;
import com.thanksd.server.dto.response.DiaryIdResponse;
import com.thanksd.server.dto.response.DiaryInfoListResponse;
import com.thanksd.server.dto.response.DiaryResponse;
import java.time.LocalDate;

public interface DiaryService {

    public DiaryIdResponse saveDiary(DiaryRequest diaryRequest, Long memberId);
    public DiaryResponse updateDiary(DiaryUpdateRequest diaryUpdateRequest, Long memberId, Long diaryId);
    public DiaryIdResponse deleteDiary(Long memberId, Long diaryId);
    public DiaryAllResponse findMemberDiaries(Long memberId);
    public DiaryResponse findOne(Long memberId, Long diaryId);
    public DiaryDateResponse findExistingDiaryDate(Long memberId, int year, int month);
    public DiaryInfoListResponse findDiaryByDate(Long memberId, LocalDate date);
}
