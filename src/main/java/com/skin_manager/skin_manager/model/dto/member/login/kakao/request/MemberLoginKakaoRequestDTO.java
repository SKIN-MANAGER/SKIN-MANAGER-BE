package com.skin_manager.skin_manager.model.dto.member.login.kakao.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginKakaoRequestDTO {
    private String code;
    private String cliendId;
    private String redirectUri;
}
