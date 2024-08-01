package com.skin_manager.skin_manager.repository.anonymous_forum;

import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AnmFrmRepo extends JpaRepository<AnmFrmEntity, Long> {


    @Transactional
    @Modifying
    @Query("Update AnmFrmEntity b set b.viewCount = b.viewCount +1 where b.postSeq = ?1")
    void countUpView(Long postSeq);
}
