package com.skin_manager.skin_manager.model.dto.anonymous_forum.response.comment;

import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmCmntEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.skin_manager.skin_manager.util.CommentDeleteStatus.Y;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnmFrmCmntRespDTO {

    private Long id;
    private String comment;
    private String nickName;

    private String password;

    private Long postId;

    private List<AnmFrmCmntRespDTO> childComments = new ArrayList<>();
    private String isDeleted;

    private Timestamp registeredAt;

    public AnmFrmCmntRespDTO(Long id, String comment, String nickName, String password, Long postId, String isDeleted, Timestamp registeredAt) {
        this.id = id;
        this.comment = comment;
        this.nickName = nickName;
        this.password = password;
        this.postId = postId;
        this.isDeleted = isDeleted;
        this.registeredAt = registeredAt;
    }

    public static AnmFrmCmntRespDTO entityToAnmFrmCmntRespDTO(AnmFrmCmntEntity anmFrmCmntEntity) {
        return anmFrmCmntEntity.getIsDeleted() == Y ?
                new AnmFrmCmntRespDTO(anmFrmCmntEntity.getCommentId(), "삭제된 댓글입니다.", null, null, null, Y.name(), null) :
                new AnmFrmCmntRespDTO(anmFrmCmntEntity.getCommentId(),  anmFrmCmntEntity.getComment(), anmFrmCmntEntity.getNickName(), anmFrmCmntEntity.getPassword(), anmFrmCmntEntity.getAnmFrm().getPostSeq(), anmFrmCmntEntity.getIsDeleted().name(), anmFrmCmntEntity.getRegisteredAt());
    }
}
