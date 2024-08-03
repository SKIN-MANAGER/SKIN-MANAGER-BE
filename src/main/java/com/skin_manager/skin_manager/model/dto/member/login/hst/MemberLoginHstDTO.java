package com.skin_manager.skin_manager.model.dto.member.login.hst;

import com.skin_manager.skin_manager.model.entity.member.login.hst.MemberLoginHstEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginHstDTO {
    private long memberLoginHstSeq;
    private long memberLoginSeq;
    private String id;
    private String pwd;
    private int pwdErrCnt;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberLoginHstEntity 필드들을 MemberLoginHstDTO로 변환
    public static MemberLoginHstDTO of(MemberLoginHstEntity memberLoginHstEntity) {
        return new MemberLoginHstDTO(
                memberLoginHstEntity.getMemberLoginHstSeq(),
                memberLoginHstEntity.getMemberLoginSeq(),
                memberLoginHstEntity.getId(),
                memberLoginHstEntity.getPwd(),
                memberLoginHstEntity.getPwdErrCnt(),
                memberLoginHstEntity.getRegDtm(),
                memberLoginHstEntity.getModDtm()
        );
    }
}
