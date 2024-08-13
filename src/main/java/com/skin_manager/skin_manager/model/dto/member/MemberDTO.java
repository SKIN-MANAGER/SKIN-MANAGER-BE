package com.skin_manager.skin_manager.model.dto.member;

import com.skin_manager.skin_manager.model.entity.member.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private long memberSeq;
    private String name;
    private String firstPhone;
    private String middlePhone;
    private String lastPhone;
    private String email;
    private String role;
    private String sns;
    private String snsId;
    private String memberYn;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberEntity 필드들을 MemberDTO로 변환
    public static MemberDTO of(MemberEntity memberEntity) {
        return new MemberDTO(
                memberEntity.getMemberSeq(),
                memberEntity.getName(),
                memberEntity.getFirstPhone(),
                memberEntity.getMiddlePhone(),
                memberEntity.getLastPhone(),
                memberEntity.getEmail(),
                memberEntity.getRole(),
                memberEntity.getSns(),
                memberEntity.getSnsId(),
                memberEntity.getMemberYn(),
                memberEntity.getRegDtm(),
                memberEntity.getModDtm()
        );
    }
}
