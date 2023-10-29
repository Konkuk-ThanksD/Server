package com.thanksd.server.service;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.response.DiaryAllResponse;
import com.thanksd.server.dto.response.DiaryIdResponse;
import com.thanksd.server.dto.response.DiaryResponse;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.exception.notfound.NotFoundMemberException;
import com.thanksd.server.repository.DiaryRepository;
import com.thanksd.server.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                new Diary(member, diaryRequest.getContent(), diaryRequest.getFont(), diaryRequest.getImage())
        );

        return new DiaryIdResponse(diary.getId());
    }

    @Transactional
    public DiaryResponse updateDiary(DiaryRequest diaryRequest,Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);
        findDiary.validateDiaryOwner(member);

        findDiary.update(
                validateData(findDiary.getContent(), diaryRequest.getContent()),
                validateData(findDiary.getFont(), diaryRequest.getFont()),
                validateData(findDiary.getImage(), diaryRequest.getImage())
        );
        Diary diary = diaryRepository.save(findDiary);

        return new DiaryResponse(diary.getContent(), diary.getFont(), diary.getImage());
    }

    private String validateData(String oldData, String newData) {
        if (newData == null) {
            return oldData;
        }
        return newData;
    }

    @Transactional
    public DiaryIdResponse deleteDiary(Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundMemberException::new);
        findDiary.validateDiaryOwner(member);

        findDiary.disConnectMember();
        diaryRepository.delete(findDiary);

        return new DiaryIdResponse(diaryId);
    }

    public DiaryAllResponse findMemberDiaries(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        List<Diary> diaries = member.getDiaries();
        return new DiaryAllResponse(getDiaryResponseList(diaries));
    }

    private List<DiaryResponse> getDiaryResponseList(List<Diary> diaries) {
        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaries) {
            diaryResponseList.add(new DiaryResponse(diary.getContent(), diary.getFont(), diary.getImage()));
        }
        return diaryResponseList;
    }

    public DiaryResponse findOne(Long memberId, Long diaryId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);
        diary.validateDiaryOwner(member);

        return new DiaryResponse(diary.getContent(), diary.getFont(), diary.getImage());
    }
}
