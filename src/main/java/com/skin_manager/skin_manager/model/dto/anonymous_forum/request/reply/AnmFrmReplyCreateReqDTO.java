package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmReplyCreateReqDTO {

    private String reply;
    private Long postSeq;
    private String nickName;
    private String password;
    private Long parentId;
    private String isDeleted;
}
