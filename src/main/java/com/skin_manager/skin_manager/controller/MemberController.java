package com.skin_manager.skin_manager.controller;

import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.response.MemberSignupResponseDTO;
import com.skin_manager.skin_manager.model.dto.memberloginhst.MemberLoginHstDTO;
import com.skin_manager.skin_manager.model.dto.memberloginhst.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.dto.memberloginhst.login.response.MemberLoginResponseDTO;
import com.skin_manager.skin_manager.service.member.MemberService;
import com.skin_manager.skin_manager.util.ResponseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     *
     * @param memberSignupRequestDTO
     * @return
     */
    @PostMapping("/signup")
    public ResponseResultCode<MemberSignupResponseDTO> signup(@RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        MemberDTO memberDTO = memberService.signup(memberSignupRequestDTO);

        return ResponseResultCode.success(MemberSignupResponseDTO.of(memberDTO));
    }

    /**
     * 로그인
     *
     * @param memberLoginRequestDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseResultCode<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberLoginHstDTO memberLoginHstDTO = memberService.login(memberLoginRequestDTO);

        return ResponseResultCode.success(MemberLoginResponseDTO.of(memberLoginHstDTO));
    }
}
