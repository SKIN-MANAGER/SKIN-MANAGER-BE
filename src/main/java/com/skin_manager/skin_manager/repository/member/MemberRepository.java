package com.skin_manager.skin_manager.repository.member;

import com.skin_manager.skin_manager.model.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findBySnsAndSnsId(String sns, String snsId);
}
