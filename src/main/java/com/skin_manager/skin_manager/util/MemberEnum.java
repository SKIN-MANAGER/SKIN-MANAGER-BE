package com.skin_manager.skin_manager.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberEnum {
    ADMIN("ADMIN"),

    USER("USER"),

    KAKAO("KAKAO"),

    NAVER("NAVER");

    private String value;
}
