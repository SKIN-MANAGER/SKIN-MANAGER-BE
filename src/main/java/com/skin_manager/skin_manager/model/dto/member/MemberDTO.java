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
    private String email;
    private String role;
    private String sns;
    private String memberYn;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberEntity 필드들을 MemberDTO로 변환
    public static MemberDTO of(MemberEntity memberEntity) {
        return new MemberDTO(
                memberEntity.getMemberSeq(),
                memberEntity.getEmail(),
                memberEntity.getRole(),
                memberEntity.getSns(),
                memberEntity.getMemberYn(),
                memberEntity.getRegDtm(),
                memberEntity.getModDtm()
        );
    }
}
