package com.skin_manager.skin_manager.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("0000", "성공"),

    FAIL("9999", "실패"),

    YES("Y", ""),

    NO("N", "");

    private String value;
    private String message;
}
