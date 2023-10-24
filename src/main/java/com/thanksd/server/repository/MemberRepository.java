package com.thanksd.server.repository;

import com.thanksd.server.domain.Member;
import com.thanksd.server.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmailAndPlatform(String email, Platform platform);

    Optional<Member> findByEmailAndPlatform(String email, Platform platform);

    Optional<Member> findByPlatformAndPlatformId(Platform platform, String platformId);

    @Query("select m.id from Member m where m.platform = :platform and m.platformId = :platformId")
    Optional<Long> findIdByPlatformAndPlatformId(Platform platform, String platformId);
}
