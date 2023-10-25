package com.thanksd.server.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.repository.DiaryRepository;
import com.thanksd.server.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@ServiceTest
@Transactional
class DiaryServiceTest {
    
    @Autowired
    private DiaryRepository diaryRepository;
    
    @Autowired
    private DiaryService diaryService;

    @Autowired
    private MemberRepository memberRepository;

    Member member = null;
    @BeforeEach
    void beforeEach() {
        member = new Member("kyj0703@konkuk.ac.kr","1111");
        memberRepository.save(member);
    }

    @Test
    @DisplayName("일기를 저장한다.")
    public void saveDiary() throws Exception {
        //given
        DiaryRequest diaryRequest = new DiaryRequest("content","sans","https://s3.~~");

        //when
        Long diaryId = diaryService.saveDiary(diaryRequest,member.getId());

        //then
        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundDiaryException());

        assertEquals(diaryRequest.getContent(),findDiary.getContent(),"저장된 일기 내용이 같아야 한다.");
        assertEquals(diaryRequest.getFont(),findDiary.getFont(),"저장된 일기 폰트가 같아야 한다.");
        assertEquals(diaryRequest.getImage(),findDiary.getImage(),"저장된 일기 폰트가 같아야 한다.");
    }

    @Test
    @DisplayName("일기 업데이트 시, 존재하지 않는 일기는 업데이트 될 수 없다.")
    public void notFoundDiaryWhenUpdate() throws Exception {
        //given
        DiaryRequest oldDiaryRequest = new DiaryRequest("oldContent","sans","https://s3.~~");
        DiaryRequest newDiaryRequest = new DiaryRequest("newContent","sans","https://s3.~~");

        //when
        Long diaryId = diaryService.saveDiary(oldDiaryRequest,member.getId());
        //then
        assertThatThrownBy(() -> diaryService.updateDiary(newDiaryRequest, diaryId+1))
                .isInstanceOf(NotFoundDiaryException.class);
    }
}