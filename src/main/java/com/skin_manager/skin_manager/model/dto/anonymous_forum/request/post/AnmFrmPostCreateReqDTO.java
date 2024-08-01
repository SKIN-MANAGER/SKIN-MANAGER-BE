package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnmFrmPostCreateReqDTO {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickName;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
