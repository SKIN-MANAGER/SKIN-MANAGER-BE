package com.skin_manager.skin_manager.model.dto.member.signup.response;

import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.util.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupResponseDTO {

    private long memberSeq;
    private String memberId;
    private String email;
    private MemberRole memberRole;
    private String sns;

    public static MemberSignupResponseDTO of(MemberDTO memberDTO) {
        return new MemberSignupResponseDTO(
                memberDTO.getMemberSeq(),
                memberDTO.getMemberId(),
                memberDTO.getEmail(),
                memberDTO.getMemberRole(),
                memberDTO.getSns()
        );
    }
}
