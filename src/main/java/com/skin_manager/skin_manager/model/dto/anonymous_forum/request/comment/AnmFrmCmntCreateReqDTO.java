package com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author  : 홍정완
 * @Date    : 20240730
 * @Explain : 익명게시판 게시글의 댓글 작성 요청 시, 데이터를 전달하는 DTO
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmCmntCreateReqDTO {

    private Long postSeq;
    private String nickName;
    private String password;
    private String content;
    private Long parentId;
    private String isDeleted;
}
