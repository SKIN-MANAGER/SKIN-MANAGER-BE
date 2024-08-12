package com.skin_manager.skin_manager.model.dto.member.login.naver.response;

import com.skin_manager.skin_manager.model.dto.member.login.kakao.AuthTokens;
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
    private AuthTokens token;
}
