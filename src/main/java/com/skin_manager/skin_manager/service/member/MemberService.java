package com.skin_manager.skin_manager.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.member.MemberDTO;
import com.skin_manager.skin_manager.model.dto.member.check.duplicate.id.request.MemberCheckDuplicateIdRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.check.duplicate.id.response.MemberCheckDuplicateIdResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.AuthTokens;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.request.MemberLoginKakaoRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.kakao.response.MemberLoginKakaoResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.request.MemberLoginNaverRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.naver.response.MemberLoginNaverResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.refresh.request.MemberLoginRefreshRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.refresh.response.MemberLoginRefreshResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.login.request.MemberLoginRequestDTO;
import com.skin_manager.skin_manager.model.dto.member.login.response.MemberLoginResponseDTO;
import com.skin_manager.skin_manager.model.dto.member.signup.request.MemberSignupRequestDTO;
import com.skin_manager.skin_manager.model.entity.member.MemberEntity;
import com.skin_manager.skin_manager.model.entity.member.hst.MemberHstEntity;
import com.skin_manager.skin_manager.model.entity.member.login.MemberLoginEntity;
import com.skin_manager.skin_manager.model.entity.member.login.hst.MemberLoginHstEntity;
import com.skin_manager.skin_manager.repository.member.MemberRepository;
import com.skin_manager.skin_manager.repository.member.hst.MemberHstRepository;
import com.skin_manager.skin_manager.repository.member.login.AuthTokensGenerator;
import com.skin_manager.skin_manager.repository.member.login.MemberLoginRepository;
import com.skin_manager.skin_manager.repository.member.login.hst.MemberLoginHstRepository;
import com.skin_manager.skin_manager.util.MemberEnum;
import com.skin_manager.skin_manager.util.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberHstRepository memberHstRepository;
    private final MemberLoginRepository memberLoginRepository;
    private final MemberLoginHstRepository memberLoginHstRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthTokensGenerator authTokensGenerator;
    private final RedisTemplate<String, String> redisTemplate;

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
    public MemberDTO signup(MemberSignupRequestDTO memberSigupRequestDTO) {
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
                memberSigupRequestDTO.getName(),
                memberSigupRequestDTO.getFirstPhone(),
                memberSigupRequestDTO.getMiddlePhone(),
                memberSigupRequestDTO.getLastPhone(),
                memberSigupRequestDTO.getEmail(),
                memberSigupRequestDTO.getRole(),
                null,
                null,
                ResultCodeEnum.YES.getValue()
        ));

        if (log.isInfoEnabled()) {
            log.info("saveMemberAndLogin Method MemberEntity : {}", memberEntity);
        }

        // member history 테이블 저장
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                memberSigupRequestDTO.getName(),
                memberSigupRequestDTO.getFirstPhone(),
                memberSigupRequestDTO.getMiddlePhone(),
                memberSigupRequestDTO.getLastPhone(),
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
                "N",
                null
        ));

        if (log.isInfoEnabled()) {
            log.info("saveMemberAndLogin Method MemberLoginEntity : {}", memberLoginEntity);
        }

        // member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberSigupRequestDTO.getId(),
                encoder.encode(memberSigupRequestDTO.getPwd()),
                memberLoginEntity.getPwdErrCnt(),
                memberLoginEntity.getAutoLogin()
        ));
        return memberEntity;
    }

    /**
     * 로그인
     *
     * @param memberLoginRequestDTO
     * @return
     */
    public MemberLoginResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        AuthTokens token;
        // 로그인 전 아이디가 존재하는지 체크
        Optional<MemberLoginEntity> memberLoginEntity = memberLoginRepository.findById(memberLoginRequestDTO.getId());

        if (log.isInfoEnabled()) {
            log.info("login Method Optional<MemberLoginEntity> {}", memberLoginEntity);
        }

        if (memberLoginEntity.isEmpty()) {
            throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
        } else {
            if (encoder.matches(memberLoginRequestDTO.getPwd(), memberLoginEntity.get().getPwd())) {
                token = saveLogin(memberLoginRequestDTO, memberLoginEntity.get());
            } else {
                throw new ApplicationContextException(ErrorCode.INVALID_ID_OR_PWD.getMessage());
            }
        }
        return new MemberLoginResponseDTO(memberLoginEntity.get().getId(), token);
    }

    /**
     * 로그인 테이블 수정
     * <p>
     * Login, Login History 테이블을 업데이트하는 메소드이다.
     *
     * @param memberLoginRequestDTO
     * @param memberLoginEntity
     */
    private AuthTokens saveLogin(MemberLoginRequestDTO memberLoginRequestDTO, MemberLoginEntity memberLoginEntity) {
        // 로그인 후 member login 테이블 수정
        memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberLoginEntity.getMemberSeq(),
                memberLoginRequestDTO.getId(),
                encoder.encode(memberLoginRequestDTO.getPwd()),
                0,
                memberLoginRequestDTO.getAutoLogin(),
                memberLoginEntity.getRegDtm()
        ));

        // 로그인 후 member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberSeq(),
                memberLoginRequestDTO.getId(),
                encoder.encode(memberLoginRequestDTO.getPwd()),
                memberLoginEntity.getPwdErrCnt(),
                memberLoginRequestDTO.getAutoLogin()
        ));

        AuthTokens token = authTokensGenerator.generate(memberLoginRequestDTO.getId());

        if (log.isInfoEnabled()) {
            log.info("saveLogin Method AuthTokens {}", token);
        }

        if (ResultCodeEnum.YES.getValue().equals(memberLoginRequestDTO.getAutoLogin())) {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인일 경우 refreshtokenexpiretime을 저장
            redisTemplate.opsForValue().set(memberLoginRequestDTO.getId(), token.getRefreshToken(), token.getRefreshTokenExpireTime(), TimeUnit.SECONDS);
        } else {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인이 아닐 경우 accesstokenexpiretime을 저장
            redisTemplate.opsForValue().set(memberLoginRequestDTO.getId(), token.getRefreshToken(), token.getAccessTokenExpireTime(), TimeUnit.SECONDS);
        }
        return token;
    }

    /**
     * 카카오로그인
     *
     * @param code
     * @param autoLogin
     * @return
     */
    public MemberLoginKakaoResponseDTO kakaoLogin(String code, String autoLogin) {
        // 인가코드로 토큰 요청
        String accessToken = getAccessToken(new MemberLoginKakaoRequestDTO(code, kakaoClientId, kakaoRedirectUri));

        // 토큰으로 사용자정보 요청
        Map<String, Object> userInfo = getKakaoInfo(accessToken);

        // 사용자정보 등록
        MemberLoginKakaoResponseDTO memberLoginKakaoResponseDTO = getKakaoLogin(userInfo, autoLogin);

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

        if (log.isInfoEnabled()) {
            log.info("getAccessToken Method JsonNode {}", jsonNode);
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

        if (log.isInfoEnabled()) {
            log.info("getKakaoInfo Method JsonNode {}", jsonNode);
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String name = jsonNode.get("kakao_account").get("name").asText();
        String phone = jsonNode.get("kakao_account").get("phone_number").asText().substring(4);

        String[] phoneArray = phone.split("-");
        String firstPhone = "0" + phoneArray[0];
        String middlePhone = phoneArray[1];
        String lastPhone = phoneArray[2];

        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("name", name);
        userInfo.put("firstPhone", firstPhone);
        userInfo.put("middlePhone", middlePhone);
        userInfo.put("lastPhone", lastPhone);

        return userInfo;
    }

    /**
     * 카카오로그인 요청
     * <p>
     * 사용자정보를 저장하고 토큰을 생성하는 메소드이다.
     *
     * @param userInfo
     * @param autoLogin
     * @return
     */
    private MemberLoginKakaoResponseDTO getKakaoLogin(Map<String, Object> userInfo, String autoLogin) {
        String uid = userInfo.get("id").toString();
        String email = userInfo.get("email").toString();
        String name = userInfo.get("name").toString();
        String firstPhone = userInfo.get("firstPhone").toString();
        String middlePhone = userInfo.get("middlePhone").toString();
        String lastPhone = userInfo.get("lastPhone").toString();
        String sns = MemberEnum.KAKAO.getValue();

        // 회원가입과 로그인을 동시에 체크
        memberLoginRepository.findById(uid).ifPresentOrElse(
                obj -> saveLogin(uid, sns, obj, autoLogin),
                () -> saveMemberAndLogin(name, firstPhone, middlePhone, lastPhone, uid, email, MemberEnum.USER.getValue(), sns, autoLogin)
        );

        AuthTokens token = authTokensGenerator.generate(uid);

        if (log.isInfoEnabled()) {
            log.info("getKakaoLogin Method AuthTokens {}", token);
        }

        if (ResultCodeEnum.YES.getValue().equals(autoLogin)) {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인일 경우 refreshtokenexpiretime을 저장
            redisTemplate.opsForValue().set(uid, token.getRefreshToken(), token.getRefreshTokenExpireTime(), TimeUnit.SECONDS);
        } else {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인이 아닐 경우 accesstokenexpiretime을 저장
            redisTemplate.opsForValue().set(uid, token.getRefreshToken(), token.getAccessTokenExpireTime(), TimeUnit.SECONDS);
        }

        return new MemberLoginKakaoResponseDTO(uid, email, name, firstPhone, middlePhone, lastPhone, token);
    }

    /**
     * 로그인 테이블 수정
     * <p>
     * Login, Login History 테이블을 업데이트하는 메소드이다.
     *
     * @param id
     * @param sns
     * @param memberLoginEntity
     * @param autoLogin
     */
    private void saveLogin(String id, String sns, MemberLoginEntity memberLoginEntity, String autoLogin) {
        // 로그인 후 member login 테이블 수정
        memberLoginRepository.save(MemberLoginEntity.createMemberLoginEntity(
                memberLoginEntity.getMemberLoginSeq(),
                memberLoginEntity.getMemberSeq(),
                id,
                encoder.encode(sns),
                0,
                autoLogin,
                memberLoginEntity.getRegDtm()
        ));

        // 로그인 후 member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberSeq(),
                id,
                encoder.encode(sns),
                memberLoginEntity.getPwdErrCnt(),
                autoLogin
        ));
    }

    /**
     * 멤버, 로그인 저장
     * <p>
     * Member, Member History, Login, Login History 테이블에 저장하는 메소드이다.
     *
     * @param name
     * @param firstPhone
     * @param middlePhone
     * @param lastPhone
     * @param id
     * @param email
     * @param role
     * @param sns
     * @param autoLogin
     */
    private void saveMemberAndLogin(String name, String firstPhone, String middlePhone, String lastPhone, String id, String email, String role, String sns, String autoLogin) {
        // member 테이블 저장
        MemberEntity memberEntity = memberRepository.save(MemberEntity.createMemberEntity(
                name,
                firstPhone,
                middlePhone,
                lastPhone,
                email,
                role,
                sns,
                id,
                ResultCodeEnum.YES.getValue()
        ));

        if (log.isInfoEnabled()) {
            log.info("saveMemberAndLogin Method MemberEntity {}", memberEntity);
        }

        // member history 테이블 저장
        memberHstRepository.save(MemberHstEntity.createMemberHstEntity(
                memberEntity.getMemberSeq(),
                name,
                firstPhone,
                middlePhone,
                lastPhone,
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
                autoLogin,
                null
        ));

        if (log.isInfoEnabled()) {
            log.info("saveMemberAndLogin Method MemberLoginEntity {}", memberLoginEntity);
        }

        // member login history 테이블 저장
        memberLoginHstRepository.save(MemberLoginHstEntity.createMemberLoginHstEntity(
                memberLoginEntity.getMemberLoginSeq(),
                id,
                encoder.encode(sns),
                memberLoginEntity.getPwdErrCnt(),
                autoLogin
        ));
    }

    /**
     * 네이버로그인
     *
     * @param code
     * @param state
     * @param autoLogin
     * @return
     */
    public MemberLoginNaverResponseDTO naverLogin(String code, String state, String autoLogin) {
        // 인가코드로 토큰 요청
        String accessToken = getAccessToken(new MemberLoginNaverRequestDTO(code, naverClientId, naverClientSecret, state));

        // 토큰으로 사용자정보 요청
        Map<String, Object> userInfo = getNaverInfo(accessToken);

        // 사용자정보 등록
        MemberLoginNaverResponseDTO memberLoginNaverResponseDTO = getNaverLogin(userInfo, autoLogin);

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
            log.info("getAccessToken Method JsonNode {}", jsonNode);
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
            log.info("getNaverInfo Method JsonNode : {}", jsonNode);
        }

        String id = jsonNode.get("response").get("id").asText();
        String email = jsonNode.get("response").get("email").asText();
        String name = jsonNode.get("response").get("name").asText();
        String phone = jsonNode.get("response").get("mobile").asText();

        String[] phoneArray = phone.split("-");
        String firstPhone = phoneArray[0];
        String middlePhone = phoneArray[1];
        String lastPhone = phoneArray[2];

        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("name", name);
        userInfo.put("firstPhone", firstPhone);
        userInfo.put("middlePhone", middlePhone);
        userInfo.put("lastPhone", lastPhone);

        return userInfo;
    }

    /**
     * 네이버로그인 요청
     * <p>
     * 사용자정보를 저장하고 토큰을 생성하는 메소드이다.
     *
     * @param userInfo
     * @param autoLogin
     * @return
     */
    private MemberLoginNaverResponseDTO getNaverLogin(Map<String, Object> userInfo, String autoLogin) {
        String uid = userInfo.get("id").toString();
        String email = userInfo.get("email").toString();
        String name = userInfo.get("name").toString();
        String firstPhone = userInfo.get("firstPhone").toString();
        String middlePhone = userInfo.get("middlePhone").toString();
        String lastPhone = userInfo.get("lastPhone").toString();
        String sns = MemberEnum.NAVER.getValue();

        // 회원가입과 로그인을 동시에 체크
        memberLoginRepository.findById(uid).ifPresentOrElse(
                obj -> saveLogin(uid, sns, obj, autoLogin),
                () -> saveMemberAndLogin(name, firstPhone, middlePhone, lastPhone, uid, email, MemberEnum.USER.getValue(), sns, autoLogin)
        );

        AuthTokens token = authTokensGenerator.generate(uid);

        if (log.isInfoEnabled()) {
            log.info("getNaverLogin Method AuthTokens : {}", token);
        }

        if (ResultCodeEnum.YES.getValue().equals(autoLogin)) {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인일 경우 refreshtokenexpiretime을 저장
            redisTemplate.opsForValue().set(uid, token.getRefreshToken(), token.getRefreshTokenExpireTime(), TimeUnit.SECONDS);
        } else {
            // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인이 아닐 경우 accesstokenexpiretime을 저장
            redisTemplate.opsForValue().set(uid, token.getRefreshToken(), token.getAccessTokenExpireTime(), TimeUnit.SECONDS);
        }
        return new MemberLoginNaverResponseDTO(uid, email, name, firstPhone, middlePhone, lastPhone, token);
    }

    /**
     * 로그인갱신
     * <p>
     * redis를 조회하여 로그인을 갱신하는 메소드이다.
     *
     * @param memberLoginRefreshRequestDTO
     * @return
     */
    public MemberLoginRefreshResponseDTO loginRefresh(MemberLoginRefreshRequestDTO memberLoginRefreshRequestDTO) {
        String token;
        AuthTokens authTokens;
        Optional<MemberEntity> memberEntity;
        Optional<MemberLoginEntity> memberLoginEntity;

        try {
            token = redisTemplate.opsForValue().get(memberLoginRefreshRequestDTO.getId());

            if (log.isInfoEnabled()) {
                log.info("autoLogin Method String : {}", token);
            }
        } catch (Exception e) {
            throw new ApplicationContextException(ErrorCode.INVALID_ID.getMessage());
        }

        if (token == null) {
            throw new ApplicationContextException(ErrorCode.BAD_REQUEST.getMessage());
        } else {
            memberLoginEntity = memberLoginRepository.findById(memberLoginRefreshRequestDTO.getId());

            if (memberLoginEntity.isEmpty()) {
                throw new ApplicationContextException(ErrorCode.NO_EXIST_MEMBER.getMessage());
            }

            if (log.isInfoEnabled()) {
                log.info("autoLogin Method Optional<MemberLoginEntity> : {}", memberLoginEntity);
            }

            authTokens = authTokensGenerator.generate(memberLoginRefreshRequestDTO.getId());

            if (log.isInfoEnabled()) {
                log.info("autoLogin Method AuthTokens : {}", authTokens);
            }

            memberEntity = memberRepository.findByMemberSeq(memberLoginEntity.get().getMemberSeq());

            if (memberEntity.isEmpty()) {
                throw new ApplicationContextException(ErrorCode.NO_EXIST_MEMBER.getMessage());
            }

            if (log.isInfoEnabled()) {
                log.info("autoLogin Method Optional<MemberEntity> : {}", memberEntity);
            }

            if (ResultCodeEnum.YES.getValue().equals(memberLoginEntity.get().getAutoLogin())) {
                // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인일 경우 refreshtokenexpiretime을 저장
                redisTemplate.opsForValue().set(memberLoginRefreshRequestDTO.getId(), authTokens.getRefreshToken(), authTokens.getRefreshTokenExpireTime(), TimeUnit.SECONDS);
            } else {
                // redis 서버에 Map<String, String> 형태로 id값과 refreshtoken값을 저장, 자동로그인이 아닐 경우 accesstokenexpiretime을 저장
                redisTemplate.opsForValue().set(memberLoginRefreshRequestDTO.getId(), authTokens.getRefreshToken(), authTokens.getAccessTokenExpireTime(), TimeUnit.SECONDS);
            }
        }
        return new MemberLoginRefreshResponseDTO(memberLoginRefreshRequestDTO.getId(), memberEntity.get().getEmail(), memberEntity.get().getName(), memberEntity.get().getFirstPhone(), memberEntity.get().getMiddlePhone(), memberEntity.get().getLastPhone(), authTokens);
    }


    /**
     * 중복아이디체크
     * <p>
     * 아이디가 중복되는지 체크하는 메소드이다.
     *
     * @param memberCheckDuplicateIdRequestDTO
     * @return
     */
    public MemberCheckDuplicateIdResponseDTO checkDuplicateId(MemberCheckDuplicateIdRequestDTO memberCheckDuplicateIdRequestDTO) {
        MemberCheckDuplicateIdResponseDTO memberCheckDuplicateIdResponseDTO = new MemberCheckDuplicateIdResponseDTO();

        // 회원가입하기 전 이미 존재하는 아이디인지 login 테이블 체크
        memberLoginRepository.findById(memberCheckDuplicateIdRequestDTO.getId()).ifPresentOrElse(
                (obj) -> {
                    memberCheckDuplicateIdResponseDTO.setDuplicate(true);
                },
                () -> {
                    memberCheckDuplicateIdResponseDTO.setDuplicate(false);
                }
        );
        return memberCheckDuplicateIdResponseDTO;
    }
}
