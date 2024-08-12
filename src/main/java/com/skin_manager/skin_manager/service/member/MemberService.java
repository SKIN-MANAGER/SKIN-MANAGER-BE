package com.skin_manager.skin_manager.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.login.MemberLoginDTO;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.AuthTokens;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.AuthTokensGenerator;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.request.MemberLoginKakaoRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.response.MemberLoginKakaoResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.request.MemberLoginNaverRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.response.MemberLoginNaverResponseDTO;
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
import com.skin_manager.skin_manager.util.MemberEnum;
import com.skin_manager.skin_manager.util.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
    private final AuthTokensGenerator authTokensGenerator;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    /**
     * 회원가입
     *
     * @param memberSigupRequestDTO
     * @return
     */
    @Transactional
    public MemberDTO signup(MemberSignupRequestDTO memberSigupRequestDTO) {
        // 회원가입하기 전 이미 존재하는 아이디인지 memberId 체크
        memberLoginRepository.findById(memberSigupRequestDTO.getId()).ifPresent(it -> {
            throw new ApplicationContextException(ErrorCode.DUPLICATED_MEMBER_ID.getMessage());
        });

        // 멤버, 로그인 저장
        MemberEntity memberEntity = saveMemberAndLogin(memberSigupRequestDTO);

        return MemberDTO.of(memberEntity);
    }

    /**
     * 멤버, 로그인 저장
     * <p>
     * Member, Member History, Login, Login History 테이블에 저장하는 메소드이다.
     *
     * @param memberSigupRequestDTO
     * @return
     */
    private MemberEntity saveMemberAndLogin(MemberSignupRequestDTO memberSigupRequestDTO) {
        // member 테이블 저장
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                null,
                null,
                ResultCodeEnum.YES.getValue()
        ));

        // member history 테이블 저장
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                null,
                null,
                memberEntity.getMemberYn()
        ));

        // member login 테이블 저장
        MemberLoginEntity memberLoginEntity = memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                null,
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                0,
                null
        ));

        // member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberLoginEntity.getPwdErrCnt()
        ));
        return memberEntity;
    }

    /**
     * 로그인
     *
     * @param memberLoginRequestDTO
     * @return
     */
    public MemberLoginDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        // 로그인 전 아이디가 존재하는지 체크
        Optional<MemberLoginEntity> memberLoginEntity = memberLoginRepository.findById(memberLoginRequestDTO.getId());

        if (memberLoginEntity.isEmpty()) {
            throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
        } else {
            // 해당 아이디의 패스워드와 입력받은 패스워드가 일치하는지 체크
            if (encoder.matches(memberLoginRequestDTO.getPwd(), memberLoginEntity.get().getPwd())) {
                // 로그인, 로그인 히스토리 테이블 수정
                saveLogin(memberLoginRequestDTO, memberLoginEntity);
            } else {
                throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
            }
        }
        return MemberLoginDTO.of(memberLoginEntity.get());
    }

    /**
     * 로그인 테이블 수정
     * <p>
     * Login, Login History 테이블을 업데이트하는 메소드이다.
     *
     * @param memberLoginRequestDTO
     * @param memberLoginEntity
     */
    private void saveLogin(MemberLoginRequestDTO memberLoginRequestDTO, Optional<MemberLoginEntity> memberLoginEntity) {
        // 로그인 후 member login 테이블 수정
        memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                memberLoginEntity.get().getMemberLoginSeq(),
                memberLoginEntity.get().getMemberSeq(),
                memberLoginRequestDTO.getId(),
                encoder.encode(memberLoginRequestDTO.getPwd()),
                0,
                memberLoginEntity.get().getRegDtm()
        ));

        // 로그인 후 member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.get().getMemberSeq(),
                memberLoginRequestDTO.getId(),
                encoder.encode(memberLoginRequestDTO.getPwd()),
                memberLoginEntity.get().getPwdErrCnt()
        ));
    }

    /**
     * 카카오로그인
     *
     * @param code
     * @return
     */
    @Transactional
    public MemberLoginKakaoResponseDTO kakaoLogin(String code) {
        // 인가코드로 토큰 요청
        String accessToken = getAccessToken(new MemberLoginKakaoRequestDTO(code, kakaoClientId, kakaoRedirectUri));

        // 토큰으로 사용자정보 요청
        Map<String, Object> userInfo = getKakaoInfo(accessToken);

        // 사용자정보 등록
        MemberLoginKakaoResponseDTO memberLoginKakaoResponseDTO = getKakaoLogin(userInfo);

        return memberLoginKakaoResponseDTO;
    }

    /**
     * 토큰 요청
     * <p>
     * 인가 코드로 카카오를 통해 토큰을 요청하는 메소드이다.
     *
     * @param kakaoLoginRequestDTO
     * @return
     */
    private String getAccessToken(MemberLoginKakaoRequestDTO kakaoLoginRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoLoginRequestDTO.getClientId());
        body.add("redirect_uri", kakaoLoginRequestDTO.getRedirectUri());
        body.add("code", kakaoLoginRequestDTO.getCode());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new ApplicationContextException(ErrorCode.TOKEN_NOT_FOUND.getMessage());
        }
        return jsonNode.get("access_token").asText();
    }

    /**
     * 카카오정보 요청
     * <p>
     * 토큰으로 카카오를 통해 정보를 요청하는 메소드이다.
     *
     * @param accessToken
     * @return
     */
    private Map<String, Object> getKakaoInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoUserInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new ApplicationContextException(ErrorCode.USER_INFO_NOT_FOUND.getMessage());
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();

        userInfo.put("id", id);
        userInfo.put("email", email);

        return userInfo;
    }

    /**
     * 카카오로그인 요청
     * <p>
     * 사용자정보를 저장하고 토큰을 생성하는 메소드이다.
     *
     * @param userInfo
     * @return
     */
    private MemberLoginKakaoResponseDTO getKakaoLogin(Map<String, Object> userInfo) {
        String uid = userInfo.get("id").toString();
        String email = userInfo.get("email").toString();
        String sns = MemberEnum.KAKAO.getValue();

        // 회원가입과 로그인을 동시에 체크
        memberLoginRepository.findById(uid).ifPresentOrElse(
                obj -> saveLogin(uid, sns, obj),
                () -> saveMemberAndLogin(uid, email, MemberEnum.USER.getValue(), sns)
        );

        AuthTokens token = authTokensGenerator.generate(uid);

        return new MemberLoginKakaoResponseDTO(uid, email, token);
    }

    /**
     * 로그인 테이블 수정
     * <p>
     * Login, Login History 테이블을 업데이트하는 메소드이다.
     *
     * @param id
     * @param sns
     * @param memberLoginEntity
     */
    private void saveLogin(String id, String sns, MemberLoginEntity memberLoginEntity) {
        // 로그인 후 member login 테이블 수정
        memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberLoginEntity.getMemberSeq(),
                id,
                encoder.encode(sns),
                0,
                memberLoginEntity.getRegDtm()
        ));

        // 로그인 후 member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberSeq(),
                id,
                encoder.encode(sns),
                memberLoginEntity.getPwdErrCnt()
        ));
    }

    /**
     * 멤버, 로그인 저장
     * <p>
     * Member, Member History, Login, Login History 테이블에 저장하는 메소드이다.
     *
     * @param id
     * @param email
     * @param role
     * @param sns
     */
    private void saveMemberAndLogin(String id, String email, String role, String sns) {
        // member 테이블 저장
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                email,
                role,
                sns,
                id,
                ResultCodeEnum.YES.getValue()
        ));

        // member history 테이블 저장
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                email,
                role,
                sns,
                id,
                memberEntity.getMemberYn()
        ));

        // member login 테이블 저장
        MemberLoginEntity memberLoginEntity = memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                null,
                memberEntity.getMemberSeq(),
                id,
                encoder.encode(sns),
                0,
                null
        ));

        // member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberLoginSeq(),
                id,
                encoder.encode(sns),
                memberLoginEntity.getPwdErrCnt()
        ));
    }

    /**
     * 네이버로그인
     *
     * @param code
     * @param state
     * @return
     */
    @Transactional
    public MemberLoginNaverResponseDTO naverLogin(String code, String state) {
        // 인가코드로 토큰 요청
        String accessToken = getAccessToken(new MemberLoginNaverRequestDTO(code, naverClientId, naverClientSecret, state));

        // 토큰으로 사용자정보 요청
        Map<String, Object> userInfo = getNaverInfo(accessToken);

        // 사용자정보 등록
        MemberLoginNaverResponseDTO memberLoginNaverResponseDTO = getNaverLogin(userInfo);

        return memberLoginNaverResponseDTO;
    }

    /**
     * 토큰 요청
     * <p>
     * 인가 코드로 네이버를 통해 토큰을 요청하는 메소드이다.
     *
     * @param memberLoginNaverRequestDTO
     * @return
     */
    private String getAccessToken(MemberLoginNaverRequestDTO memberLoginNaverRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", memberLoginNaverRequestDTO.getClientId());
        body.add("client_secret", memberLoginNaverRequestDTO.getClientSecret());
        body.add("code", memberLoginNaverRequestDTO.getCode());
        body.add("state", memberLoginNaverRequestDTO.getState());

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, naverTokenRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new ApplicationContextException(ErrorCode.TOKEN_NOT_FOUND.getMessage());
        }

        if (log.isInfoEnabled()) {
            log.info("naverLogin Service getAccessToken : " + jsonNode);
        }
        return jsonNode.get("access_token").asText();
    }

    /**
     * 네이버정보 요청
     * <p>
     * 토큰으로 네이버를 통해 정보를 요청하는 메소드이다.
     *
     * @param accessToken
     * @return
     */
    private Map<String, Object> getNaverInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.POST, naverUserInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new ApplicationContextException(ErrorCode.USER_INFO_NOT_FOUND.getMessage());
        }

        if (log.isInfoEnabled()) {
            log.info("naverLogin Service getNaverInfo : " + jsonNode);
        }

        String id = jsonNode.get("response").get("id").asText();
        String email = jsonNode.get("response").get("email").asText();

        userInfo.put("id", id);
        userInfo.put("email", email);

        return userInfo;
    }

    /**
     * 네이버로그인 요청
     * <p>
     * 사용자정보를 저장하고 토큰을 생성하는 메소드이다.
     *
     * @param userInfo
     * @return
     */
    private MemberLoginNaverResponseDTO getNaverLogin(Map<String, Object> userInfo) {
        String uid = userInfo.get("id").toString();
        String email = userInfo.get("email").toString();
        String sns = MemberEnum.NAVER.getValue();

        // 회원가입과 로그인을 동시에 체크
        memberLoginRepository.findById(uid).ifPresentOrElse(
                obj -> saveLogin(uid, sns, obj),
                () -> saveMemberAndLogin(uid, email, MemberEnum.USER.getValue(), sns)
        );

        AuthTokens token = authTokensGenerator.generate(uid);

        return new MemberLoginNaverResponseDTO(uid, email, token);
    }
}
