package com.skin_manager.skin_manager.model.dto.anonymous_forum.response.reply;

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
public class AnmFrmReplyRespDTO {

    private Long id;

    private String reply;
    private String nickName;

    private String password;

    private Long postSeq;

    private List<AnmFrmReplyRespDTO> childComments = new ArrayList<>();
    private String isDeleted;

    private Timestamp registeredAt;

    public AnmFrmReplyRespDTO(Long id, String reply, String nickName, String password, Long postSeq, String isDeleted, Timestamp registeredAt) {
        this.id = id;
        this.reply = reply;
        this.nickName = nickName;
        this.password = password;
        this.postSeq = postSeq;
        this.isDeleted = isDeleted;
        this.registeredAt = registeredAt;
    }

    public static AnmFrmReplyRespDTO entityToAnmFrmReplyRespDTO(AnmFrmCmntEntity anmFrmCmntEntity) {
        return anmFrmCmntEntity.getIsDeleted() == Y ?
                new AnmFrmReplyRespDTO(anmFrmCmntEntity.getCommentId(), "삭제된 댓글입니다.", null, null, null, Y.name(), null) :
                new AnmFrmReplyRespDTO(anmFrmCmntEntity.getCommentId(), anmFrmCmntEntity.getComment(), anmFrmCmntEntity.getNickName(), anmFrmCmntEntity.getPassword(), anmFrmCmntEntity.getAnmFrm().getPostSeq(), anmFrmCmntEntity.getIsDeleted().name(), anmFrmCmntEntity.getRegisteredAt());
    }
}
