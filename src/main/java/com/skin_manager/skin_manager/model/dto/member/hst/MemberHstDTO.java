package com.skin_manager.skin_manager.model.dto.member.hst;

import com.skin_manager.skin_manager.model.entity.member.hst.MemberHstEntity;
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
    private String email;
    private String role;
    private String sns;
    private String memberYn;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberHstEntity 필드들을 MemberHstDTO로 변환
    public static MemberHstDTO of(MemberHstEntity memberHstEntity) {
        return new MemberHstDTO(
                memberHstEntity.getMemberHstSeq(),
                memberHstEntity.getMemberSeq(),
                memberHstEntity.getEmail(),
                memberHstEntity.getRole(),
                memberHstEntity.getSns(),
                memberHstEntity.getMemberYn(),
                memberHstEntity.getRegDtm(),
                memberHstEntity.getModDtm()
        );
    }
}
