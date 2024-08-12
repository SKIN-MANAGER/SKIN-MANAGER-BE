package com.skin_manager.skin_manager.model.entity.member.hst;

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

    public static MemberHstEntity createMemberHstEntity(long memberSeq, String email, String role, String sns, String snsId, String memberYn) {
        MemberHstEntity memberHstEntity = new MemberHstEntity();
        memberHstEntity.setMemberSeq(memberSeq);
        memberHstEntity.setEmail(email);
        memberHstEntity.setRole(role);
        memberHstEntity.setSns(sns);
        memberHstEntity.setSnsId(snsId);
        memberHstEntity.setMemberYn(memberYn);
        return memberHstEntity;
    }
}
