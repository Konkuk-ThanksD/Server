package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMemberAndCreatedTimeBetween(Member member, LocalDateTime start, LocalDateTime end);

    @Query("SELECT d FROM Diary d WHERE d.member = :member AND DATE(d.createdTime) = :date")
    List<Diary> findDiariesByCreatedTime(@Param("member") Member member, @Param("date") LocalDate date);
}
