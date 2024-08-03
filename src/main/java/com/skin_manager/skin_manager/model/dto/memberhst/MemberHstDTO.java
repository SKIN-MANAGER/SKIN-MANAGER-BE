package com.skin_manager.skin_manager.model.dto.memberhst;

import com.skin_manager.skin_manager.model.entity.memberhst.MemberHstEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberHstDTO {
    private long memberHstSeq;
    private long memberSeq;
    private String id;
    private String pwd;
    private String email;
    private String role;
    private String sns;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberHstEntity 필드들을 MemberHstDTO로 변환
    public static MemberHstDTO of(MemberHstEntity memberHstEntity) {
        return new MemberHstDTO(
                memberHstEntity.getMemberHstSeq(),
                memberHstEntity.getMemberSeq(),
                memberHstEntity.getId(),
                memberHstEntity.getPwd(),
                memberHstEntity.getEmail(),
                memberHstEntity.getRole(),
                memberHstEntity.getSns(),
                memberHstEntity.getRegDtm(),
                memberHstEntity.getModDtm()
        );
    }
}
