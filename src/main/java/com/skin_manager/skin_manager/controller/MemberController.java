package com.skin_manager.skin_manager.controller;

import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.check.duplicate.id.request.MemberCheckDuplicateIdRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.check.duplicate.id.response.MemberCheckDuplicateIdResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.response.MemberLoginKakaoResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.response.MemberLoginNaverResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.refresh.request.MemberLoginRefreshRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.refresh.response.MemberLoginRefreshResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.response.MemberLoginResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.response.MemberSignupResponseDTO;
import com.skin_manager.skin_manager.service.member.MemberService;
import com.skin_manager.skin_manager.util.ResponseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
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

        if (log.isInfoEnabled()) {
            log.info("signup Controller Success : {}", memberDTO.toString());
        }
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
        MemberLoginResponseDTO memberLoginResponseDTO = memberService.login(memberLoginRequestDTO);

        if (log.isInfoEnabled()) {
            log.info("login Controller Success : {}", memberLoginResponseDTO.toString());
        }
        return ResponseResultCode.success(memberLoginResponseDTO);
    }

    /**
     * 카카오로그인
     *
     * @param code
     * @param autoLogin
     * @return
     */
    @GetMapping("/kakao/login")
    public ResponseResultCode<MemberLoginKakaoResponseDTO> kakaoLogin(@RequestParam String code, @RequestParam String autoLogin) {
        MemberLoginKakaoResponseDTO memberLoginKakaoResponseDTO = memberService.kakaoLogin(code, autoLogin);

        if (log.isInfoEnabled()) {
            log.info("kakaoLogin Controller Success : {}", memberLoginKakaoResponseDTO.toString());
        }
        return ResponseResultCode.success(memberLoginKakaoResponseDTO);
    }

    /**
     * 네이버로그인
     *
     * @param code
     * @param state
     * @param autoLogin
     * @return
     */
    @GetMapping("/naver/login")
    public ResponseResultCode<MemberLoginNaverResponseDTO> naverLogin(@RequestParam String code, @RequestParam String state, @RequestParam String autoLogin) {
        MemberLoginNaverResponseDTO memberLoginNaverResponseDTO = memberService.naverLogin(code, state, autoLogin);

        if (log.isInfoEnabled()) {
            log.info("naverLogin Controller Success : {}", memberLoginNaverResponseDTO.toString());
        }
        return ResponseResultCode.success(memberLoginNaverResponseDTO);
    }

    /**
     * 로그인갱신
     *
     * @param memberLoginRefreshRequestDTO
     * @return
     */
    @PostMapping("/login/refresh")
    public ResponseResultCode<MemberLoginRefreshResponseDTO> loginRefresh(@RequestBody MemberLoginRefreshRequestDTO memberLoginRefreshRequestDTO) {
        MemberLoginRefreshResponseDTO memberLoginRefreshResponseDTO = memberService.loginRefresh(memberLoginRefreshRequestDTO);

        if (log.isInfoEnabled()) {
            log.info("loginRefresh Controller Success : {}", memberLoginRefreshResponseDTO.toString());
        }
        return ResponseResultCode.success(memberLoginRefreshResponseDTO);
    }

    /**
     * 중복아이디체크
     *
     * @param memberCheckDuplicateIdRequestDTO
     * @return
     */
    @PostMapping("/id/duplicate/check")
    public ResponseResultCode<MemberCheckDuplicateIdResponseDTO> checkDuplicateId(@RequestBody MemberCheckDuplicateIdRequestDTO memberCheckDuplicateIdRequestDTO) {
        MemberCheckDuplicateIdResponseDTO memberCheckDuplicateIdResponseDTO = memberService.checkDuplicateId(memberCheckDuplicateIdRequestDTO);

        if (log.isInfoEnabled()) {
            log.info("checkDuplicateId Controller Success : {}", memberCheckDuplicateIdResponseDTO.toString());
        }
        return ResponseResultCode.success(memberCheckDuplicateIdResponseDTO);
    }
}
