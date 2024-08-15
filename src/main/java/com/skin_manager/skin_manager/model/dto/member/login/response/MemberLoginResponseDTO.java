package com.skin_manager.skin_manager.model.dto.member.login.response;

import com.skin_manager.skin_manager.model.dto.member.login.AuthTokens;
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
    private String id;
    private AuthTokens token;
}
