package com.skin_manager.skin_manager.model.dto.member.signup.response;

import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupResponseDTO {

    private long memberSeq;
    private String id;
    private String email;
    private String role;
    private String sns;

    public static MemberSignupResponseDTO of(MemberDTO memberDTO) {
        return new MemberSignupResponseDTO(
                memberDTO.getMemberSeq(),
                memberDTO.getId(),
                memberDTO.getEmail(),
                memberDTO.getRole(),
                memberDTO.getSns()
        );
    }
}
