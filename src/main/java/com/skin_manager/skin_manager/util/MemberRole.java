package com.skin_manager.skin_manager.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ADMIN("ADMIN"),

    USER("USER");

    private String value;
}
