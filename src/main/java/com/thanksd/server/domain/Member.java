package com.thanksd.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "platform"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "platform_id")
    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nation")
    private Nation nation;

    public Member(String email, String password, Platform platform, String platformId, Nation nation) {
        this.email = email;
        this.password = password;
        this.platform = platform;
        this.platformId = platformId;
        this.nation = nation;
    }

    public Member(String email, String password) {
        this(email, password, Platform.THANKSD, null, Nation.KOREA);
    }

    public Member(String email, Platform platform, String platformId, Nation nation) {
        this.email = email;
        this.platform = platform;
        this.platformId = platformId;
        this.nation = nation;
    }

    public void registerOAuthMember(String email, Nation nation) {
        if (email != null) {
            this.email = email;
            this.nation = nation;
        }
    }
}
