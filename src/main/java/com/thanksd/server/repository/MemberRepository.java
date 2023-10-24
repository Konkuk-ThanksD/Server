package com.thanksd.server.repository;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmailAndPlatform(String email, Platform platform);
}
