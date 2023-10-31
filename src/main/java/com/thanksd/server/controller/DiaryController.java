package com.thanksd.server.controller;

import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.request.DiaryUpdateRequest;
import com.thanksd.server.dto.response.*;

import com.thanksd.server.security.auth.LoginUserId;
import com.thanksd.server.service.DiaryService;
import com.thanksd.server.service.PreSignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Tag(name = "Diaries", description = "일기")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;
    private final PreSignedUrlService presignedUrlService;
    private final String prefixImagePath = "images";

    @Operation(summary = "모든 일기 불러오기")
    @GetMapping
    public Response<Object> findAllDairy(@LoginUserId Long memberId) {
        DiaryAllResponse response = diaryService.findMemberDiaries(memberId);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "특정 일기 작성")
    @PostMapping
    public Response<Object> saveDiary(@LoginUserId Long memberId,
                                      @Valid @RequestBody DiaryRequest diaryRequest) {

        DiaryIdResponse response = diaryService.saveDiary(diaryRequest, memberId);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "특정 일기 조회")
    @GetMapping("/{id}")
    public Response<Object> findDiary(@LoginUserId Long memberId, @PathVariable Long id) {
        DiaryResponse response = diaryService.findOne(memberId, id);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "특정 일기 수정")
    @PutMapping("/{id}")
    public Response<Object> updateDiary(@LoginUserId Long memberId, @PathVariable Long id,
                                        @RequestBody DiaryUpdateRequest diaryUpdateRequest) {
        DiaryResponse response = diaryService.updateDiary(diaryUpdateRequest, memberId, id);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "특정 일기 삭제")
    @DeleteMapping("/{id}")
    public Response<Object> deleteDiary(@LoginUserId Long memberId, @PathVariable Long id) {

        DiaryIdResponse response = diaryService.deleteDiary(memberId, id);
        return Response.ofSuccess("OK", response);
    }

    @Operation(summary = "이미지 업로드를 위한 presigned url 요청")
    @PostMapping("/presigned")
    public String preSignedUrl(@LoginUserId Long memberId,@RequestParam("image") String imageName){
        return presignedUrlService.getPreSignedUrl(prefixImagePath,imageName,memberId);
    }

    @Operation(summary = "해당 달에 일기 존재하는 날짜 조회")
    @GetMapping("/calendar")
    public Response<Object> findExistingDiaryDate(
            @LoginUserId Long memberId,
            @RequestParam("year") final int year,
            @RequestParam("month") final int month) {
        DiaryDateResponse response = diaryService.findExistingDiaryDate(memberId, year, month);
        return Response.ofSuccess("OK", response);
    }
}