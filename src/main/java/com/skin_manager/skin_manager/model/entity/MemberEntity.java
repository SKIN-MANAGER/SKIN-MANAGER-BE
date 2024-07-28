package com.skin_manager.skin_manager.model.entity;

import com.skin_manager.skin_manager.util.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberSeq;

    @Column(length = 30, nullable = false, unique = true)
    private String memberId;

    @Column(length = 100, nullable = false)
    private String pwd;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.USER;

    @Column(length = 20, nullable = false)
    private String sns;

    @Column(name = "reg_dtm")
    private Timestamp regDtm;

    @Column(name = "mod_dtm")
    private Timestamp modDtm;

    @PrePersist
    void regDtm() {
        this.regDtm = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void modDtm() {
        this.modDtm = Timestamp.from(Instant.now());
    }

    public static MemberEntity createMemberEntity(String memberId, String password, String email, MemberRole memberRole, String sns) {
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setMemberId(memberId);
        memberEntity.setPwd(password);
        memberEntity.setEmail(email);
        memberEntity.setMemberRole(memberRole.USER);
        memberEntity.setSns(sns);

        return memberEntity;
    }
}
