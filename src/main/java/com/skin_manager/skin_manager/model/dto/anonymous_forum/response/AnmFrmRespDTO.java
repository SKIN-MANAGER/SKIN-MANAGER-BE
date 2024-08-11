package com.skin_manager.skin_manager.model.dto.anonymous_forum.response;

import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnmFrmRespDTO {

    private long postSeq;

    private String nickName;

    private String password;

    private String title;

    private String content;

    private int vw_cnt;

    private int rcmnd_cnt;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    // MemberEntity에 있는 필드들을 MemberDTO 클래스로 변환하는 메소드
    public static AnmFrmRespDTO of(AnmFrmEntity anmFrmEntity) {
        return new AnmFrmRespDTO(
                anmFrmEntity.getPostSeq(),
                anmFrmEntity.getNickName(),
                anmFrmEntity.getPassword(),
                anmFrmEntity.getTitle(),
                anmFrmEntity.getContent().toString(),
                anmFrmEntity.getViewCount(),
                0,
                anmFrmEntity.getRegisteredAt(),
                anmFrmEntity.getModifiedAt()
        );
    }
}
