package com.thanksd.server.domain;

import com.thanksd.server.exception.badrequest.MemberMismatchException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content")
    private String content;

    @Column(name = "font")
    private String font;

    @Column(name = "image")
    private String image;


    public Diary(Member member, String content, String font, String image) {
        setMember(member);
        this.content = content;
        this.font = font;
        this.image = image;
    }

    public Diary(Member member) {
        this(member, null, null, null);
    }

    private void setMember(Member member) {
        this.member = member;
        member.getDiaries().add(this);
    }

    public void update(String content, String font, String image) {
        this.content = content;
        this.font = font;
        this.image = image;
    }

    public void disConnectMember(){
        this.member = null;
    }

    public void validateDiaryOwner(Member member) {
        if(this.member.getId()!= member.getId()){
            throw new MemberMismatchException();
        }
    }
}
