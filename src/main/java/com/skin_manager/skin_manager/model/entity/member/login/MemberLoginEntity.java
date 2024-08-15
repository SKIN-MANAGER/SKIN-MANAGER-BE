package com.skin_manager.skin_manager.model.entity.member.login;

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
@Table(name = "MEMBER_LOGIN")
public class MemberLoginEntity {
    @Id
    @Column(name = "MEMBER_LOGIN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberLoginSeq;

    @Column(name = "MEMBER_SEQ")
    private long memberSeq;

    @Column(name = "ID")
    private String id;

    @Column(name = "PWD")
    private String pwd;

    @Column(name = "PWD_ERR_CNT")
    private int pwdErrCnt;

    @Column(name = "AUTO_LOGIN")
    private String autoLogin;

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

    public static MemberLoginEntity createMemberLoginEntity(Long memberLoginSeq, long memberSeq, String id, String pwd, int pwdErrCnt, String autoLogin, Timestamp regDtm) {
        MemberLoginEntity memberLoginEntity = new MemberLoginEntity();
        memberLoginEntity.setMemberLoginSeq(memberLoginSeq);
        memberLoginEntity.setMemberSeq(memberSeq);
        memberLoginEntity.setId(id);
        memberLoginEntity.setPwd(pwd);
        memberLoginEntity.setPwdErrCnt(pwdErrCnt);
        memberLoginEntity.setAutoLogin(autoLogin);
        memberLoginEntity.setRegDtm(regDtm);
        return memberLoginEntity;
    }
}
