package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import com.thanksd.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMemberAndCreatedTimeBetween(Member member, LocalDateTime start, LocalDateTime end);
}
