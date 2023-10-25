package com.thanksd.server.service;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.exception.notfound.NotFoundMemberException;
import com.thanksd.server.repository.DiaryRepository;
import com.thanksd.server.repository.MemberRepository;
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
    public Long saveDiary(DiaryRequest diaryRequest,Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        Diary diary = diaryRepository.save(
                new Diary(member,diaryRequest.getContent(),diaryRequest.getFont(),diaryRequest.getImage())
        );

        return diary.getId();
    }

    @Transactional
    public void updateDiary(DiaryRequest diaryRequest, Long diaryId) {
        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundDiaryException());
        findDiary.setContent(validateData(findDiary.getContent(),diaryRequest.getContent()));
        findDiary.setFont(validateData(findDiary.getFont(),diaryRequest.getFont()));
        findDiary.setImage(validateData(findDiary.getImage(),diaryRequest.getImage()));
        diaryRepository.save(findDiary);
        /*
        diaryRepository.update(
                diaryId,
                validateData(findDiary.getContent(),content),
                validateData(findDiary.getFont(),font),
                validateData(findDiary.getImage(),image)
        );
         //*/
    }

    private String validateData(String oldData, String newData) {
        if(newData == null)
            return oldData;
        return newData;
    }

    @Transactional
    public void deleteDiary(Long diaryId) {
        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundDiaryException());
        diaryRepository.delete(findDiary);
    }

    public List<Diary> findMemberDiaries(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        return diaryRepository.findAllByMember(member)
                .orElseThrow(() -> new NotFoundDiaryException());
    }

    public Diary findOne(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundDiaryException());
    }
}
