package com.thanksd.server.controller;

import com.thanksd.server.dto.request.DiaryRequest;
import com.thanksd.server.dto.response.DiaryAllResponse;
import com.thanksd.server.dto.response.DiaryIdResponse;
import com.thanksd.server.dto.response.DiaryResponse;
import com.thanksd.server.dto.response.Response;
import com.thanksd.server.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Diaries", description = "일기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/member/{id}")
    public Response<Object> findAllDairy(@PathVariable("id") Long memberId){
        DiaryAllResponse response = diaryService.findMemberDiaries(memberId);
        return Response.ofSuccess("OK", response);
    }

    @PostMapping("/member/{id}")
    public Response<Object> saveDiary(@PathVariable("id") Long memberId,
                                      @RequestBody DiaryRequest diaryRequest) {
        DiaryIdResponse response = diaryService.saveDiary(diaryRequest, memberId);
        return Response.ofSuccess("OK", response);
    }

    @GetMapping("/{id}")
    public Response<Object> findDiary(@PathVariable Long id) {
        DiaryResponse response = diaryService.findOne(id);
        return Response.ofSuccess("OK", response);
    }

    @PutMapping("/{id}")
    public Response<Object> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest) {
        DiaryResponse response = diaryService.updateDiary(diaryRequest, id);
        return Response.ofSuccess("OK", response);
    }

    @DeleteMapping("/{id}")
    public Response<Object> deleteDiary(@PathVariable Long id) {
        DiaryIdResponse response = diaryService.deleteDiary(id);
        return Response.ofSuccess("OK", response);
    }
}
