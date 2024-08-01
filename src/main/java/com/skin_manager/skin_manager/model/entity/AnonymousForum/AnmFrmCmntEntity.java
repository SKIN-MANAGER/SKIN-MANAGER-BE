package com.skin_manager.skin_manager.model.entity.AnonymousForum;

import com.skin_manager.skin_manager.util.CommentDeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "anm_frm_cmnt")
public class AnmFrmCmntEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq")
    private AnmFrmEntity anmFrm;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(nullable = false)
    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private AnmFrmCmntEntity parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<AnmFrmCmntEntity> childComments = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private CommentDeleteStatus isDeleted;

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

    public static AnmFrmCmntEntity getAnmFrmCmntEntity(String nickName, String password, AnmFrmEntity postSeq, AnmFrmCmntEntity parentComment, String comment) {
        AnmFrmCmntEntity entity = new AnmFrmCmntEntity();

        entity.setNickName(nickName);
        entity.setPassword(password);
        entity.setAnmFrm(postSeq);
        entity.setComment(comment);
        entity.setParentComment(parentComment);
        entity.setIsDeleted(CommentDeleteStatus.N);

        return entity;
    }

    public void changeCommentDeleteStatus(CommentDeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
