package com.thanksd.server.repository;

import com.thanksd.server.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {}
