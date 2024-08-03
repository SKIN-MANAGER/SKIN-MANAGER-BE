package com.skin_manager.skin_manager.service.member;

import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.login.MemberLoginDTO;
import com.skin_manager.skin_manager.model.dto.member.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.entity.member.MemberEntity;
import com.skin_manager.skin_manager.model.entity.member.hst.MemberHstEntity;
import com.skin_manager.skin_manager.model.entity.member.login.MemberLoginEntity;
import com.skin_manager.skin_manager.model.entity.member.login.hst.MemberLoginHstEntity;
import com.skin_manager.skin_manager.repository.member.MemberRepository;
import com.skin_manager.skin_manager.repository.member.hst.MemberHstRepository;
import com.skin_manager.skin_manager.repository.member.login.MemberLoginRepository;
import com.skin_manager.skin_manager.repository.member.login.hst.MemberLoginHstRepository;
import com.skin_manager.skin_manager.util.ResultCode;
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
    private final MemberLoginRepository memberLoginRepository;
    private final MemberLoginHstRepository memberLoginHstRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public MemberDTO signup(MemberSignupRequestDTO memberSigupRequestDTO) {
        /**
         * 1. 회원가입하기 전 이미 존재하는 아이디인지 memberId 체크
         */
        memberLoginRepository.findById(memberSigupRequestDTO.getId()).ifPresent(it -> {
            throw new ApplicationContextException(ErrorCode.DUPLICATED_MEMBER_ID.getMessage());
        });

        /**
         * 2. member 테이블 저장
         */
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                memberSigupRequestDTO.getSns(),
                ResultCode.YES.getValue()
        ));

        /**
         * 3. member history 테이블 저장
         */
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                memberSigupRequestDTO.getSns(),
                memberEntity.getMemberYn()
        ));

        /**
         * 4. member login 테이블 저장
         */
        MemberLoginEntity memberLoginEntity = memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                null,
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                0,
                null
        ));

        /**
         * 5. member login history 테이블 저장
         */
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberLoginEntity.getPwdErrCnt()
        ));
        return MemberDTO.of(memberEntity);
    }

    public MemberLoginDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        /**
         * 1. 로그인 전 아이디가 존재하는지 체크
         */
        Optional<MemberLoginEntity> memberLoginEntity = memberLoginRepository.findById(memberLoginRequestDTO.getId());

        if (memberLoginEntity.isEmpty()) {
            throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
        } else {
            /**
             * 2. 해당 아이디의 패스워드와 입력받은 패스워드가 일치하는지 체크
             */
            if (encoder.matches(memberLoginRequestDTO.getPwd(), memberLoginEntity.get().getPwd())) {
                /**
                 * 3. 로그인 후 member login 테이블 수정
                 */
                memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                        memberLoginEntity.get().getMemberLoginSeq(),
                        memberLoginEntity.get().getMemberSeq(),
                        memberLoginRequestDTO.getId(),
                        encoder.encode(memberLoginRequestDTO.getPwd()),
                        0,
                        memberLoginEntity.get().getRegDtm()
                ));

                /**
                 * 4. 로그인 후 member login history 테이블 저장
                 */
                memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                        memberLoginEntity.get().getMemberSeq(),
                        memberLoginRequestDTO.getId(),
                        encoder.encode(memberLoginRequestDTO.getPwd()),
                        memberLoginEntity.get().getPwdErrCnt()
                ));
            } else {
                throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
            }
        }
        return MemberLoginDTO.of(memberLoginEntity.get());
    }
}
