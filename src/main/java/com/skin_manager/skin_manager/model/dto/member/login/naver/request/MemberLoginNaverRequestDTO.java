package com.skin_manager.skin_manager.model.dto.member.login.naver.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginNaverRequestDTO {
    private String code;
    private String clientId;
    private String clientSecret;
    private String state;
}
