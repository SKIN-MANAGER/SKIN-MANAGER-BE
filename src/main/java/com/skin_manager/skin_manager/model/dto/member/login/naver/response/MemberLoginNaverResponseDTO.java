package com.skin_manager.skin_manager.model.dto.member.login.naver.response;

import com.skin_manager.skin_manager.model.dto.member.login.AuthTokens;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginNaverResponseDTO {
    private String id;
    private String email;
    private String name;
    private String firstPhone;
    private String middlePhone;
    private String lastPhone;
    private AuthTokens token;
}
