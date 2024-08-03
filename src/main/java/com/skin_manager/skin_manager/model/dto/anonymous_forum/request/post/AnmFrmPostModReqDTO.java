package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmPostModReqDTO {

    private Long postSeq;
    private String title;
    private String content;
    private String nickName;
    private String password;
}