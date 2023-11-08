package com.thanksd.server.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.request.DiaryUpdateRequest;
import com.thanksd.server.dto.response.DiaryDateResponse;
import com.thanksd.server.dto.response.DiaryInfoListResponse;
import com.thanksd.server.dto.response.DiaryResponse;
import com.thanksd.server.exception.badrequest.InvalidDateException;
import com.thanksd.server.exception.badrequest.MemberMismatchException;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.repository.DiaryRepository;
import com.thanksd.server.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ServiceTest
class DiaryServiceTest {

    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    @Qualifier("diaryServiceTestImpl")
    private DiaryService diaryService;

    Member member = null;
    Member secondMember = null;
    String imageUrl;

    @BeforeEach
    void beforeEach() {
        member = new Member("kyj0703@konkuk.ac.kr", "qwer1111");
        secondMember = new Member("0703kyj@naver.com", "qwer1111");
        memberRepository.save(member);
        memberRepository.save(secondMember);
        imageUrl = "https://thanksd-image-bucket.s3.ap-northeast-2.amazonaws.com/images/1/cc4342b5-126f-4c73-a0b7-f70ad0a58ea6_test.jpg";
    }

    @Test
    @DisplayName("일기를 저장한다.")
    public void saveDiary() {
        //given
        DiaryRequest diaryRequest = new DiaryRequest("content", "sans",imageUrl);

        //when
        Long diaryId = diaryService.saveDiary(diaryRequest, member.getId()).getId();

        //then
        Diary findDiary = diaryRepository.findById(diaryId)
                .orElseThrow(NotFoundDiaryException::new);
        List<DiaryResponse> diaries = diaryService.findMemberDiaries(member.getId()).getDiaries();

        assertThat(diaries.size()).isEqualTo(1);
        assertEquals(diaryRequest.getContent(), findDiary.getContent(), "저장된 일기 내용이 같아야 한다.");
        assertEquals(diaryRequest.getFont(), findDiary.getFont(), "저장된 일기 폰트가 같아야 한다.");
        assertEquals("images/1/cc4342b5-126f-4c73-a0b7-f70ad0a58ea6_test.jpg", findDiary.getImage(), "저장된 일기 이미지가 같아야 한다.");
    }

    @Test
    @DisplayName("일기를 수정한다.")
    public void updateDiary() {
        //given
        DiaryRequest oldDiaryRequest = new DiaryRequest("oldContent", "sans",imageUrl);
        DiaryUpdateRequest newDiaryRequest = new DiaryUpdateRequest("newContent", "sans", imageUrl);

        //when
        Long diaryId = diaryService.saveDiary(oldDiaryRequest,member.getId()).getId();
        diaryService.updateDiary(newDiaryRequest, member.getId(), diaryId);

        //then
        DiaryResponse findDiaryResponse = diaryService.findOne(member.getId(), diaryId);
        assertEquals(newDiaryRequest.getContent(), findDiaryResponse.getContent(), "수정된 일기 내용이 같아야 한다.");
        assertEquals(newDiaryRequest.getFont(), findDiaryResponse.getFont(), "수정된 일기 폰트가 같아야 한다.");
        assertEquals("images/1/cc4342b5-126f-4c73-a0b7-f70ad0a58ea6_test.jpg", findDiaryResponse.getImage(), "수정된 일기 이미지가 같아야 한다.");
    }

    @Test
    @DisplayName("일기를 삭제한다.")
    public void deleteDiary() {
        //given
        DiaryRequest diaryRequest = new DiaryRequest("content", "sans",imageUrl);

        //when
        Long diaryId = diaryService.saveDiary(diaryRequest,member.getId()).getId();

        //then
        List<DiaryResponse> diaries = diaryService.findMemberDiaries(member.getId()).getDiaries();
        assertThat(diaries.size()).isEqualTo(1);

        diaryService.deleteDiary(member.getId(), diaryId);
        List<DiaryResponse> deleteDiaries = diaryService.findMemberDiaries(member.getId()).getDiaries();
        assertThat(deleteDiaries.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("일기 업데이트 시, 존재하지 않는 일기는 업데이트 될 수 없다.")
    public void notFoundDiaryWhenUpdate() {
        //given
        DiaryRequest oldDiaryRequest = new DiaryRequest("oldContent", "sans",imageUrl);
        DiaryUpdateRequest newDiaryRequest = new DiaryUpdateRequest("newContent", "sans", imageUrl);

        //when
        Long diaryId = diaryService.saveDiary(oldDiaryRequest, member.getId()).getId();

        //then
        assertThatThrownBy(() -> diaryService.updateDiary(newDiaryRequest, member.getId(), diaryId + 1))
                .isInstanceOf(NotFoundDiaryException.class);
    }

    @Test
    @DisplayName("업데이트 시, 공백값이 들어온 경우 기존 값을 유지한다.")
    public void notChangeValueWhenBlankRequest() {
        //given
        DiaryRequest oldDiaryRequest = new DiaryRequest("oldContent", "sans",imageUrl);
        DiaryUpdateRequest newDiaryRequest = new DiaryUpdateRequest("", "", "");

        //when
        Long diaryId = diaryService.saveDiary(oldDiaryRequest,member.getId()).getId();
        diaryService.updateDiary(newDiaryRequest, member.getId(), diaryId);

        //then
        DiaryResponse findDiaryResponse = diaryService.findOne(member.getId(), diaryId);
        assertEquals(oldDiaryRequest.getContent(), findDiaryResponse.getContent(), "기존 일기 내용이 같아야 한다.");
        assertEquals(oldDiaryRequest.getFont(), findDiaryResponse.getFont(), "기존 일기 폰트가 같아야 한다.");
        assertEquals("images/1/cc4342b5-126f-4c73-a0b7-f70ad0a58ea6_test.jpg", findDiaryResponse.getImage(), "기존 일기 이미지가 같아야 한다.");
    }

    @Test
    @DisplayName("일기는 작성자만 접근가능하다.")
    public void accessDiary() {
        //given
        DiaryRequest diaryRequest = new DiaryRequest("content", "sans", imageUrl);
        DiaryUpdateRequest diaryUpdateRequest = new DiaryUpdateRequest("newContent", "sans", imageUrl);

        //when
        Long diaryId = diaryService.saveDiary(diaryRequest, member.getId()).getId();

        //then
        assertThatThrownBy(() -> diaryService.findOne(secondMember.getId(), diaryId))
                .isInstanceOf(MemberMismatchException.class);
        assertThatThrownBy(() -> diaryService.updateDiary(diaryUpdateRequest,secondMember.getId(), diaryId))
                .isInstanceOf(MemberMismatchException.class);
        assertThatThrownBy(() -> diaryService.deleteDiary(secondMember.getId(), diaryId))
                .isInstanceOf(MemberMismatchException.class);
    }

    @Test
    @DisplayName("해당 달에 존재하는 일기가 없다면 빈 리스트를 반환한다")
    public void getExistingDiaryDateWhenNotSavedDiary() {
        DiaryDateResponse findDiaryDate = diaryService.findExistingDiaryDate(member.getId(), 2023, 1);

        assertThat(findDiaryDate.getDateList().size()).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 년도와 달에 대해 달력을 조회하려 하면 예외를 반환한다")
    @ValueSource(ints = {0, 13, 22222})
    void getExistingDiaryDateByWrongDate(int wrongDate) {
        assertThrows(InvalidDateException.class,
                () -> diaryService.findExistingDiaryDate(member.getId(), wrongDate, wrongDate));
    }

    @Test
    @DisplayName("해당 날짜에 존재하는 일기가 없다면 빈 리스트를 반환한다")
    public void findDiaryByDate() {
        DiaryInfoListResponse findDiaryInfoList = diaryService.findDiaryByDate(member.getId(), LocalDate.ofEpochDay(2023-01-12));

        assertThat(findDiaryInfoList.getDiaryList().size()).isEqualTo(0);
    }
}
