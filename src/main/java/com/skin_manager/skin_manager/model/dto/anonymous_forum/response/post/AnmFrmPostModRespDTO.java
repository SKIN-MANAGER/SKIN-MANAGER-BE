package com.skin_manager.skin_manager.model.dto.anonymous_forum.response.post;

import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AnmFrmPostModRespDTO {

    private Long postSeq;

    private String title;

    private byte[] content;

    private String nickName;

    private String password;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    public static AnmFrmPostModRespDTO getAnmFrmPostModRespDTO(AnmFrmEntity anmFrmEntity) {
        return new AnmFrmPostModRespDTO(
                anmFrmEntity.getPostSeq(),
                anmFrmEntity.getTitle(),
                anmFrmEntity.getContent(),
                anmFrmEntity.getNickName(),
                anmFrmEntity.getPassword(),
                anmFrmEntity.getRegisteredAt(),
                anmFrmEntity.getModifiedAt()
        );
    }
}
