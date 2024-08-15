package com.skin_manager.skin_manager.model.dto.member.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long accessTokenExpireTime;
    private Long refreshTokenExpireTime;

    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long accessTokenExpireTime, Long refreshTokenExpireTime) {
        return new AuthTokens(accessToken, refreshToken, grantType, accessTokenExpireTime, refreshTokenExpireTime);
    }
}