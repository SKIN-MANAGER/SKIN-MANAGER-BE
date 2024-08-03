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

    public static MemberEntity createMemberEntity(String id, String password, String email, String role, String sns) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(id);
        memberEntity.setPwd(password);
        memberEntity.setEmail(email);
        memberEntity.setRole(role);
        memberEntity.setSns(sns);
        return memberEntity;
    }
}
