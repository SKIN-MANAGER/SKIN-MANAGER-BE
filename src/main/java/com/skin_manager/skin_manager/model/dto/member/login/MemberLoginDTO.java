package com.skin_manager.skin_manager.model.dto.member.login;

import com.skin_manager.skin_manager.model.entity.member.login.MemberLoginEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDTO {
    private long memberLoginSeq;
    private long memberSeq;
    private String id;
    private String pwd;
    private int pwdErrCnt;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberEntity 필드들을 MemberDTO로 변환
    public static MemberLoginDTO of(MemberLoginEntity memberLoginEntity) {
        return new MemberLoginDTO(
                memberLoginEntity.getMemberLoginSeq(),
                memberLoginEntity.getMemberSeq(),
                memberLoginEntity.getId(),
                memberLoginEntity.getPwd(),
                memberLoginEntity.getPwdErrCnt(),
                memberLoginEntity.getRegDtm(),
                memberLoginEntity.getModDtm()
        );
    }
}
