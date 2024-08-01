package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmReplyDelReqDTO {

    private Long commentId;
    private Long parentId;
    private String password;
}
