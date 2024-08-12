package com.skin_manager.skin_manager.controller;

import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.login.MemberLoginDTO;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.response.MemberLoginKakaoResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.response.MemberLoginNaverResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.response.MemberLoginResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.response.MemberSignupResponseDTO;
import com.skin_manager.skin_manager.service.member.MemberService;
import com.skin_manager.skin_manager.util.ResponseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
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
        try {
            MemberDTO memberDTO = memberService.signup(memberSignupRequestDTO);

            return ResponseResultCode.success(MemberSignupResponseDTO.of(memberDTO));
        } catch (Exception e) {
            log.error(e.getMessage());

            throw new ResponseStatusException(ErrorCode.SIGNUP_NOT_FOUND.getHttpStatus(), ErrorCode.SIGNUP_NOT_FOUND.getMessage());
        }
    }

    /**
     * 로그인
     *
     * @param memberLoginRequestDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseResultCode<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        try {
            MemberLoginDTO memberLoginDTO = memberService.login(memberLoginRequestDTO);

            return ResponseResultCode.success(MemberLoginResponseDTO.of(memberLoginDTO));
        } catch (Exception e) {
            log.error(e.getMessage());

            throw new ResponseStatusException(ErrorCode.LOGIN_NOT_FOUND.getHttpStatus(), ErrorCode.LOGIN_NOT_FOUND.getMessage());
        }
    }

    /**
     * 카카오로그인
     *
     * @param code
     * @return
     */
    @GetMapping("/kakao/login")
    public ResponseResultCode<MemberLoginKakaoResponseDTO> kakaoLogin(@RequestParam String code) {
        try {
            MemberLoginKakaoResponseDTO memberLoginKakaoResponseDTO = memberService.kakaoLogin(code);

            return ResponseResultCode.success(memberLoginKakaoResponseDTO);
        } catch (Exception e) {
            log.error(e.getMessage());

            throw new ResponseStatusException(ErrorCode.KAKAO_NOT_FOUND.getHttpStatus(), ErrorCode.KAKAO_NOT_FOUND.getMessage());
        }
    }

    /**
     * 네이버로그인
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/naver/login")
    public ResponseResultCode<MemberLoginNaverResponseDTO> naverLogin(@RequestParam String code, @RequestParam String state) {
        try {
            MemberLoginNaverResponseDTO memberLoginNaverResponseDTO = memberService.naverLogin(code, state);

            if (log.isInfoEnabled()) {
                log.info("naverLogin Controller : " + memberLoginNaverResponseDTO.toString());
            }
            return ResponseResultCode.success(memberLoginNaverResponseDTO);
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("naverLogin Controller : " + e.getMessage());
            }
            throw new ResponseStatusException(ErrorCode.NAVER_NOT_FOUND.getHttpStatus(), ErrorCode.NAVER_NOT_FOUND.getMessage());
        }
    }
}
