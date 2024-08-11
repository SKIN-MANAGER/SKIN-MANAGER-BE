package com.skin_manager.skin_manager.repository.anonymous_forum;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmCmntEntity;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.QAnmFrmCmntEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.skin_manager.skin_manager.model.entity.AnonymousForum.QAnmFrmCmntEntity.anmFrmCmntEntity;

@RequiredArgsConstructor
public class AnmFrmCmntRepoImpl implements QueryDslAnmFrmCmntRepo {

    private final JPAQueryFactory queryFactory;

    // 익명 갤러리 댓글 찾기
    @Override
    public List<AnmFrmCmntEntity> findCommentByAnmFrm(Long postSeq) {
        return queryFactory.selectFrom(anmFrmCmntEntity)
                .leftJoin(anmFrmCmntEntity.parentComment)
                .fetchJoin()
                .where(anmFrmCmntEntity.anmFrm.postSeq.eq(postSeq))
                .orderBy(anmFrmCmntEntity.parentComment.commentId.asc().nullsFirst())
                .fetch();
    }

    // 익명갤러리 각 게시글 별 댓글 수
    @Override
    public Long countAnmFrmCmntEntityByPostSeq(Long postSeq) {
        return queryFactory.select(anmFrmCmntEntity.count())
                .from(anmFrmCmntEntity)
                .where(anmFrmCmntEntity.anmFrm.postSeq.eq(postSeq))
                .fetchOne();
    }

    // 익명갤러리 댓글 update
    @Override
    public void updateAnmFrmCmnt(AnmFrmCmntEntity commentEntity) {
        queryFactory.update(QAnmFrmCmntEntity.anmFrmCmntEntity)
                .where(QAnmFrmCmntEntity.anmFrmCmntEntity.commentId.eq(commentEntity.getCommentId()))         // 댓글 ID로 조건 설정
                .set(QAnmFrmCmntEntity.anmFrmCmntEntity.comment, commentEntity.getComment())                  // 댓글 내용 업데이트
                .execute();
    }
}
