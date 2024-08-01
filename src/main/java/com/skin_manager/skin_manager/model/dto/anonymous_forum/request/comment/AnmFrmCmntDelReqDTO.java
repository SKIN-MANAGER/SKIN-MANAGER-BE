package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmCmntDelReqDTO {

    private Long commentId;
    private String password;
}
