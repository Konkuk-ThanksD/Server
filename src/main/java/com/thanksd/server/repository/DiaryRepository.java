package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    /*
    @Modifying
    @Query("update Diary d SET d.content = :content, d.font = :font, d.image = :image WHERE d.diary_id = :diary_id")
    void update(@Param("diary_id") Long diaryId, @Param("content")String content, @Param("font")String font,@Param("image")String image);
    //*/
}
