package com.skin_manager.skin_manager.model.dto.member.login.response;

import com.skin_manager.skin_manager.model.dto.member.login.MemberLoginDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {
    private long memberLoginSeq;
    private long memberSeq;
    private String id;
    private String pwd;
    private int pwdErrCnt;

    public static MemberLoginResponseDTO of(MemberLoginDTO memberLoginDTO) {
        return new MemberLoginResponseDTO(
                memberLoginDTO.getMemberLoginSeq(),
                memberLoginDTO.getMemberSeq(),
                memberLoginDTO.getId(),
                memberLoginDTO.getPwd(),
                memberLoginDTO.getPwdErrCnt()
        );
    }
}
