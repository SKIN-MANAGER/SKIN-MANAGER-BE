package com.skin_manager.skin_manager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_MEMBER_ID(HttpStatus.CONFLICT, "회원가입하려는 아이디가 이미 존재합니다."),     // memberId 중복 에러코드

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "작성하신 아이디는 회원가입이 가능한 아이디 입니다."),

    INVALID_ID_OR_PWD(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 틀립니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러 발생"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),

    INVALID_POST_PERMISSION(HttpStatus.UNAUTHORIZED, "해당 게시글 작성자가 아닙니다."),

    ALREADY_RECOMMENDED(HttpStatus.CONFLICT, "해당 회원은 이미 게시글을 추천한 상태입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
