package com.skin_manager.skin_manager.repository.member.login;

import com.skin_manager.skin_manager.model.entity.member.login.MemberLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLoginRepository extends JpaRepository<MemberLoginEntity, Integer> {
    Optional<MemberLoginEntity> findById(String id);
}
