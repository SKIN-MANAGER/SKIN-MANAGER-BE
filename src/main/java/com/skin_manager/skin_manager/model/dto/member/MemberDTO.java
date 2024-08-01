package com.skin_manager.skin_manager.model.dto.member;

import com.skin_manager.skin_manager.model.entity.MemberEntity;
import com.skin_manager.skin_manager.util.MemberRole;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private long memberSeq;
    private String memberId;
    private String pwd;
    private String email;
    private MemberRole memberRole;
    private String sns;
    private Timestamp regDtm;
    private Timestamp modDtm;

    // MemberEntity 필드들을 MemberDTO로 변환
    public static MemberDTO of(MemberEntity memberEntity) {
        return new MemberDTO(
                memberEntity.getMemberSeq(),
                memberEntity.getMemberId(),
                memberEntity.getPwd(),
                memberEntity.getEmail(),
                memberEntity.getMemberRole(),
                memberEntity.getSns(),
                memberEntity.getRegDtm(),
                memberEntity.getModDtm()
        );
    }
}
