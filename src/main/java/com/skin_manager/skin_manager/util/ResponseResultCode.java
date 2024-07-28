package com.skin_manager.skin_manager.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseResultCode<T> {

    private String resultCode;
    private T result;
    private ResponseEntity response;

    // 작업을 실패할 경우
    public static ResponseResultCode<Void> error(String errorCode) {
        return new ResponseResultCode<>(errorCode, null, null);
    }

    // 작업을 성공할 경우
    public static <T> ResponseResultCode<T> success(T result) {
        return new ResponseResultCode<>("SUCCESS", result, ResponseEntity.ok("로그인 성공"));
    }
}
