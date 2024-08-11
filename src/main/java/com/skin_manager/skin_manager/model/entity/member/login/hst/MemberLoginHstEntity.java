package com.skin_manager.skin_manager.model.entity.member.login.hst;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER_LOGIN_HST")
public class MemberLoginHstEntity {
    @Id
    @Column(name = "MEMBER_LOGIN_HST_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberLoginHstSeq;

    @Column(name = "MEMBER_LOGIN_SEQ")
    private long memberLoginSeq;

    @Column(name = "ID")
    private String id;

    @Column(name = "PWD")
    private String pwd;

    @Column(name = "PWD_ERR_CNT")
    private int pwdErrCnt;

    @Column(name = "REG_DTM")
    private Timestamp regDtm;

    @Column(name = "MOD_DTM")
    private Timestamp modDtm;

    @PrePersist
    public void regDtm() {
        this.regDtm = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void modDtm() {
        this.modDtm = Timestamp.from(Instant.now());
    }

    public static MemberLoginHstEntity createMemberLoginHstEntity(long memberLoginSeq, String id, String pwd, int pwdErrCnt) {
        MemberLoginHstEntity memberLoginHstEntity = new MemberLoginHstEntity();
        memberLoginHstEntity.setMemberLoginSeq(memberLoginSeq);
        memberLoginHstEntity.setId(id);
        memberLoginHstEntity.setPwd(pwd);
        memberLoginHstEntity.setPwdErrCnt(pwdErrCnt);
        return memberLoginHstEntity;
    }
}
