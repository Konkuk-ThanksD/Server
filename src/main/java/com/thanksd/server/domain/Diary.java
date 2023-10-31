package com.thanksd.server.domain;

import com.thanksd.server.exception.badrequest.InvalidImageUrlException;
import com.thanksd.server.exception.badrequest.MemberMismatchException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.content = content;
        this.font = font;
        setImage(image);
        setMember(member);
    }

    public Diary(Member member) {
        this(member, null, null, null);
    }

    private void setMember(Member member) {
        this.member = member;
        member.getDiaries().add(this);
    }

    private void setImage(String image) {
        URL url;
        try {
            url = new URL(image);
        } catch (MalformedURLException e) {
            throw new InvalidImageUrlException();
        }
        this.image = url.getPath();
    }

    public void update(String content, String font, String image) {
        this.content = content;
        this.font = font;
        setImage(image);
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
