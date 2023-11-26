package com.thanksd.server.service;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.request.DiaryUpdateRequest;
import com.thanksd.server.dto.response.*;
import com.thanksd.server.exception.badrequest.InvalidDateException;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.exception.notfound.NotFoundMemberException;
import com.thanksd.server.repository.DiaryRepository;
import com.thanksd.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DiaryIdResponse saveDiary(DiaryRequest diaryRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundDiaryException::new);

        Diary diary = diaryRepository.save(
                new Diary(member, diaryRequest.getImage())
        );

        return new DiaryIdResponse(diary.getId());
    }

    @Transactional
    public DiaryResponse updateDiary(DiaryUpdateRequest diaryUpdateRequest, Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);

        findDiary.validateDiaryOwner(member);
        findDiary.update(
                validateData(findDiary.getImage(), diaryUpdateRequest.getImage())
        );
        Diary diary = diaryRepository.save(findDiary);

        return new DiaryResponse(diary.getImage());
    }

    private String validateData(String oldData, String newData) {
        if (newData.isBlank()) {
            return oldData;
        }
        return newData;
    }

    @Transactional
    public DiaryIdResponse deleteDiary(Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);

        findDiary.validateDiaryOwner(member);
        findDiary.disConnectMember();
        diaryRepository.delete(findDiary);

        return new DiaryIdResponse(diaryId);
    }

    public DiaryAllResponse findMemberDiaries(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        List<Diary> diaries = member.getDiaries();
        return new DiaryAllResponse(getDiaryIdResponseList(diaries));
    }

    private List<DiaryIdResponse> getDiaryIdResponseList(List<Diary> diaries) {
        List<DiaryIdResponse> diaryIdResponses = new ArrayList<>();
        for (Diary diary : diaries) {
            diaryIdResponses.add(new DiaryIdResponse(diary.getId()));
        }
        return diaryIdResponses;
    }

    public Diary findOne(Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);
        diary.validateDiaryOwner(member);

        return diary;
    }

    public DiaryDateResponse findExistingDiaryDate(Long memberId, int year, int month) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        validateYearAndMonth(year, month);
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        Timestamp start = Timestamp.valueOf(startDate);
        Timestamp end = Timestamp.valueOf(endDate);

        List<LocalDate> dateList = getDiaryDates(member, start, end);

        return new DiaryDateResponse(dateList);
    }

    public void validateYearAndMonth(int year, int month) {
        try {
            LocalDateTime.of(year, month, 1, 0, 0);
        } catch (DateTimeException e) {
            throw new InvalidDateException();
        }
    }

    private List<LocalDate> getDiaryDates(Member member, Timestamp start, Timestamp end) {
        List<Diary> diaries = diaryRepository.findByMemberAndCreatedTimeBetween(member, start, end);
        return diaries.stream()
                .map(diary -> diary.getCreatedTime().toLocalDateTime().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    public DiaryInfoListResponse findDiaryByDate(Long memberId, LocalDate date) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        Timestamp findDate = convertToTimestamp(date);
        List<DiaryInfoResponse> diaryInfoList = getDiaryList(member, findDate);
        return new DiaryInfoListResponse(diaryInfoList);
    }

    private Timestamp convertToTimestamp(LocalDate date) {
        return Timestamp.valueOf(date.atStartOfDay());
    }

    private List<DiaryInfoResponse> getDiaryList(Member member, Timestamp date) {
        List<Diary> diaries = diaryRepository.findDiariesByCreatedTime(member, date);
        return diaries.stream()
                .map(diary -> new DiaryInfoResponse(diary.getId(), diary.getImage()))
                .collect(Collectors.toList());
    }

    public DiaryWeekCountResponse findDiaryCountByWeek(Long memberId, LocalDate today) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        Map<String, Integer> diaryCounts = new HashMap<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            int diaryCount = diaryRepository.findDiariesByCreatedTime(member, convertToTimestamp(date)).size();
            diaryCounts.put(date.getDayOfWeek().name(), diaryCount);
        }
        return new DiaryWeekCountResponse(diaryCounts);
    }
}
