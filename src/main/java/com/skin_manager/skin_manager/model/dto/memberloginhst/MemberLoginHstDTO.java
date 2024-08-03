package com.skin_manager.skin_manager.model.dto.memberloginhst;

import com.skin_manager.skin_manager.model.entity.memberloginhst.MemberLoginHstEntity;
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
    private long memberSeq;
    private String id;
    private String pwd;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberLoginHstEntity 필드들을 MemberLoginHstDTO로 변환
    public static MemberLoginHstDTO of(MemberLoginHstEntity memberLoginHstEntity) {
        return new MemberLoginHstDTO(
                memberLoginHstEntity.getMemberLoginHstSeq(),
                memberLoginHstEntity.getMemberSeq(),
                memberLoginHstEntity.getId(),
                memberLoginHstEntity.getPwd(),
                memberLoginHstEntity.getRegDtm(),
                memberLoginHstEntity.getModDtm()
        );
    }
}
