package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMemberAndCreatedTimeBetween(Member member, Timestamp createdTime, Timestamp createdTime2);

    @Query("SELECT d FROM Diary d WHERE d.member = :member AND DATE(d.createdTime) = :date")
    List<Diary> findDiariesByCreatedTime(@Param("member") Member member, @Param("date") Timestamp date);
}
