package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnmFrmPostDelReqDTO {

    private Long postSeq;
    private String password;
}
