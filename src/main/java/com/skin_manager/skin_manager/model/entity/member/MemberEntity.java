package com.skin_manager.skin_manager.model.entity.member;

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
@Table(name = "MEMBER")
public class MemberEntity {
    @Id
    @Column(name = "MEMBER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberSeq;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FIRST_PHONE")
    private String firstPhone;

    @Column(name = "MIDDLE_PHONE")
    private String middlePhone;

    @Column(name = "LAST_PHONE")
    private String lastPhone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "SNS")
    private String sns;

    @Column(name = "SNS_ID")
    private String snsId;

    @Column(name = "MEMBER_YN")
    private String memberYn;

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

    public static MemberEntity createMemberEntity(String name, String firstPhone, String middlePhone, String lastPhone, String email, String role, String sns, String snsId, String memberYn) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(name);
        memberEntity.setFirstPhone(firstPhone);
        memberEntity.setMiddlePhone(middlePhone);
        memberEntity.setLastPhone(lastPhone);
        memberEntity.setEmail(email);
        memberEntity.setRole(role);
        memberEntity.setSns(sns);
        memberEntity.setSnsId(snsId);
        memberEntity.setMemberYn(memberYn);
        return memberEntity;
    }
}
