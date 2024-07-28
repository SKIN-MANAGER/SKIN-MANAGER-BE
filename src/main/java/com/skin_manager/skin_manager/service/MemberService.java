package com.skin_manager.skin_manager.service;

import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.MemberDTO;
import com.skin_manager.skin_manager.model.dto.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.entity.MemberEntity;
import com.skin_manager.skin_manager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public MemberDTO signup(MemberSignupRequestDTO memberSigupRequestDTO) {
        // 1. 회원가입하려는 memberId가 이미 존재하는 지
        memberRepository.findByMemberId(memberSigupRequestDTO.getMemberId()).ifPresent(it -> {
            throw new ApplicationContextException(ErrorCode.DUPLICATED_MEMBER_ID.getMessage());
        });

        // 2. 회원가입 진행
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                memberSigupRequestDTO.getMemberId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getMemberRole(),
                memberSigupRequestDTO.getSns()
        ));

        return MemberDTO.of(memberEntity);
    }
}
