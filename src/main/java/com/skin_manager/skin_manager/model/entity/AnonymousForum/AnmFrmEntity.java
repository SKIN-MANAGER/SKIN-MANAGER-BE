package com.skin_manager.skin_manager.model.entity.AnonymousForum;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "anm_frm")
public class AnmFrmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "password", nullable = false)
    private String password;

    //    @Size(min = 2, max=40, message = "제목은 2자이상 40자 이하입니다.")
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "BLOB", nullable = false)
    private byte[] content;

    @Column(name = "vw_cnt", nullable = false)
    @ColumnDefault("0")
    private int viewCount;

    @OneToMany(mappedBy = "anmFrm", orphanRemoval = true)
    private List<AnmFrmCmntEntity> comments = new ArrayList<>();

    @Column(name = "reg_at")
    private Timestamp registeredAt;

    @Column(name = "mod_at")
    private Timestamp modifiedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = Timestamp.from(Instant.now());
    }

    public static AnmFrmEntity getAnmFrmEntity(String title, byte[] content, String nickName, String password) {
        AnmFrmEntity forumEntity = new AnmFrmEntity();
        forumEntity.setTitle(title);
        forumEntity.setContent(content);
        forumEntity.setNickName(nickName);
        forumEntity.setPassword(password);

        return forumEntity;
    }

    public AnmFrmEntity(String title, byte[] content, String nickName, String password) {
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.password = password;
    }

    // 수정이 필요할 경우
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    // 게시글 수정 시 호출
    public void updateContent(String title, byte[] content) {
        this.title = title;
        this.content = content;

        // 수정일 갱신
        modifiedAt();
    }

    // 게시글 조회 시 호출
    public void increaseViewCount() {
        this.viewCount++;
    }
}
