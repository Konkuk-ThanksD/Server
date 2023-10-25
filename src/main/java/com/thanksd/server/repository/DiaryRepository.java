package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d where d.member = :member")
    Optional<List<Diary>> findAllByMember(@Param("member") Member member);
//    @Modifying
//    @Query("update Diary d SET d.content = :content, d.font = :font, d.image = :image WHERE d.diary_id = :diary_id")
//    void update(@Param("diary_id") Long diaryId, @Param("content")String content, @Param("font")String font,@Param("image")String image);
}
