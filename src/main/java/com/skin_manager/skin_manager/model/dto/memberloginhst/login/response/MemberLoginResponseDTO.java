package com.skin_manager.skin_manager.model.dto.memberloginhst.login.response;

import com.skin_manager.skin_manager.model.dto.memberloginhst.MemberLoginHstDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {
    private long memberLoginHstSeq;
    private long memberSeq;
    private String id;

    public static MemberLoginResponseDTO of(MemberLoginHstDTO memberLoginHstDTO) {
        return new MemberLoginResponseDTO(
                memberLoginHstDTO.getMemberLoginHstSeq(),
                memberLoginHstDTO.getMemberSeq(),
                memberLoginHstDTO.getId()
        );
    }
}
