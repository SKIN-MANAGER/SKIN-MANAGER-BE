package com.skin_manager.skin_manager.model.entity.memberhst;

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
@Table(name = "MEMBER_HST")
public class MemberHstEntity {
    @Id
    @Column(name = "MEMBER_HST_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberHstSeq;

    @Column(name = "MEMBER_SEQ")
    private long memberSeq;

    @Column(name = "ID")
    private String id;

    @Column(name = "PWD")
    private String pwd;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "SNS")
    private String sns;

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

    public static MemberHstEntity createMemberHstEntity(long memberSeq, String id, String pwd, String email, String role, String sns) {
        MemberHstEntity memberHstEntity = new MemberHstEntity();
        memberHstEntity.setMemberSeq(memberSeq);
        memberHstEntity.setId(id);
        memberHstEntity.setPwd(pwd);
        memberHstEntity.setEmail(email);
        memberHstEntity.setRole(role);
        memberHstEntity.setSns(sns);
        return memberHstEntity;
    }
}
