package com.skin_manager.skin_manager.repository.anonymous_forum;

import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmCmntEntity;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnmFrmCmntRepo extends JpaRepository<AnmFrmCmntEntity, Long>, QueryDslAnmFrmCmntRepo {

    @Query("select c from AnmFrmCmntEntity c left join fetch c.parentComment where c.commentId = :commentId and c.password = :password")
    AnmFrmCmntEntity findCommentEntityByCommentIdWithParentCommentAndPassword(@Param("commentId") Long commentId, @Param("password") String password);

//    @Query("select c from CommentEntity c left join fetch c.parentComment where c.commentId = :commentId and c.parentComment = :parentId and c.password = :password")
//    CommentEntity findCommentEntityByCommentIdWithParentIdAndPassword(@Param("commentId") Long commentId, @Param("parentId") Long parentId, @Param("password") String password);

    @Query("select c from AnmFrmCmntEntity c left join fetch c.parentComment p where c.commentId = :commentId and p.commentId = :parentId and c.password = :password")
    AnmFrmCmntEntity findCommentEntityByCommentIdWithParentIdAndPassword(@Param("commentId") Long commentId, @Param("parentId") Long parentId, @Param("password") String password);

    Page<AnmFrmCmntEntity> findCommentEntityByAnmFrm(AnmFrmEntity anmFrm, Pageable pageable);
}
