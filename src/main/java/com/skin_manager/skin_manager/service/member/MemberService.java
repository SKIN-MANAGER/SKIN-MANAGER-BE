package com.skin_manager.skin_manager.service.member;

import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.dto.memberloginhst.MemberLoginHstDTO;
import com.skin_manager.skin_manager.model.dto.memberloginhst.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.entity.member.MemberEntity;
import com.skin_manager.skin_manager.model.entity.memberhst.MemberHstEntity;
import com.skin_manager.skin_manager.model.entity.memberloginhst.MemberLoginHstEntity;
import com.skin_manager.skin_manager.repository.member.MemberRepository;
import com.skin_manager.skin_manager.repository.memberhst.MemberHstRepository;
import com.skin_manager.skin_manager.repository.memberloginhst.MemberLoginHstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberHstRepository memberHstRepository;
    private final MemberLoginHstRepository memberLoginHstRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public MemberDTO signup(MemberSignupRequestDTO memberSigupRequestDTO) {
        /**
         * 1. 회원가입하기 전 이미 존재하는 아이디인지 memberId 체크
         */
        memberRepository.findById(memberSigupRequestDTO.getId()).ifPresent(it -> {
            throw new ApplicationContextException(ErrorCode.DUPLICATED_MEMBER_ID.getMessage());
        });

        /**
         * 2. 회원가입 진행 - member 테이블 저장
         */
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                memberSigupRequestDTO.getSns()
        ));

        /**
         * 2. 회원가입 진행 - member history 테이블 저장
         */
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                memberSigupRequestDTO.getSns()
        ));
        return MemberDTO.of(memberEntity);
    }

    public MemberLoginHstDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        /**
         * 1. 로그인 전 아이디가 일치하는지 체크
         */
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberLoginRequestDTO.getId());

        if (memberEntity.isEmpty()) {
            throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
        } else {
            /**
             * 2. 해당 아이디의 패스워드와 입력받은 패스워드가 일치하는지 체크
             */
            if (encoder.matches(memberLoginRequestDTO.getPwd(), memberEntity.get().getPwd())) {
                /**
                 * 3. 로그인 후 member login history 테이블 저장
                 */
                MemberLoginHstEntity memberLoginHstEntity = memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                        memberEntity.get().getMemberSeq(),
                        memberLoginRequestDTO.getId(),
                        encoder.encode(memberLoginRequestDTO.getPwd())
                ));
                return MemberLoginHstDTO.of(memberLoginHstEntity);
            } else {
                throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
            }
        }
    }
}
