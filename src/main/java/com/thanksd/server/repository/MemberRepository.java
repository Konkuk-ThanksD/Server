package com.thanksd.server.repository;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmailAndPlatform(String email, Platform platform);

    Optional<Member> findByEmailAndPlatform(String email, Platform platform);

    Optional<Member> findByPlatformAndPlatformId(Platform platform, String platformId);
}
