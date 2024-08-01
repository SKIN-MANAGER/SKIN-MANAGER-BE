//package com.skin_manager.skin_manager.service.AnonymousForum;
//
//import com.skin_manager.skin_manager.exception.ErrorCode;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post.AnmFrmPostDelReqDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post.AnmFrmPostModReqDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.search.AnmFrmSrchCondDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.AnmFrmRespDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.page.AnmFrmPaginationDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.post.AnmFrmPostModRespDTO;
//import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
//import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmRepo;
//import com.skin_manager.skin_manager.repository.anonymous_forum.CstAnmFrmRepo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContextException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AnmFrmService {
//
//    private final CstAnmFrmRepo cstAnmFrmRepo;
//
//    private final AnmFrmRepo anmFrmRepo;
//
//    // 게시글 목록 조회
//    public ResponseEntity<List<AnmFrmRespDTO>> getForumBoardList(Pageable pageable, AnmFrmSrchCondDTO anmFrmSrchCondDTO) {
//
//        List<AnmFrmRespDTO> forumResponseDTOList = new ArrayList<>();
//
//        Page<AnmFrmEntity> forumEntities = cstAnmFrmRepo.findAllBySearchCondition(pageable, anmFrmSrchCondDTO);
//
//        for (AnmFrmEntity anmFrmEntity : forumEntities) {
//            AnmFrmRespDTO forumResponseDTO = AnmFrmRespDTO.builder()
//                    .postSeq(anmFrmEntity.getPostSeq())
////                    .content(generalForumEntity.getContent())
//                    .title(anmFrmEntity.getTitle())
//                    .nickName(anmFrmEntity.getNickName())
//                    .vw_cnt(anmFrmEntity.getViewCount())
//                    .registeredAt(anmFrmEntity.getRegisteredAt())
//                    .modifiedAt(anmFrmEntity.getModifiedAt())
//                    .build();
//
//            forumResponseDTOList.add(forumResponseDTO);
//        }
//
//        AnmFrmPaginationDTO forumPaginationDTO = new AnmFrmPaginationDTO(
//                (int)forumEntities.getTotalElements()
//                , pageable.getPageNumber() + 1
//                , pageable.getPageSize()
//        );
//
//        return ResponseEntity.ok(forumResponseDTOList);
//    }
//
//    // 게시글 상세 조회
//    public AnmFrmRespDTO getForumPostData(Long postSeq) {
//
//        AnmFrmRespDTO forumResponseDTO = new AnmFrmRespDTO();
//
//        // 게시글이 존재하는 지
//        AnmFrmEntity forumEntity = anmFrmRepo.findById(postSeq).orElseThrow(() ->
//                new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage()));
//
//        // 게시글 조회수 증가
//        increaseViewCount(postSeq);
//
//        log.info("postId : " + forumEntity.getPostSeq());
//        log.info("title : " + forumEntity.getTitle());
//        log.info("content : " + forumEntity.getContent());
//        log.info("nickName: " + forumEntity.getNickName());
//        log.info("viewCount : " + forumEntity.getViewCount());
//        log.info("registeredAt : " + forumEntity.getRegisteredAt());
//        log.info("modifiedAt : " + forumEntity.getModifiedAt());
//
//        forumResponseDTO.setPostSeq(forumEntity.getPostSeq());
//        forumResponseDTO.setTitle(forumEntity.getTitle());
//        forumResponseDTO.setContent(new String(forumEntity.getContent(), StandardCharsets.UTF_8));
//        forumResponseDTO.setNickName(forumEntity.getNickName());
//        forumResponseDTO.setVw_cnt(forumEntity.getViewCount());
//        forumResponseDTO.setRegisteredAt(forumEntity.getRegisteredAt());
//        forumResponseDTO.setModifiedAt(forumEntity.getModifiedAt());
//
//        return forumResponseDTO;
//    }
//
//    // 게시글 조회수 증가
//    @Transactional
//    public void increaseViewCount(Long postSeq) {
//        anmFrmRepo.countUpView(postSeq);
//    }
//
//    // 게시글 작성
//    @Transactional
//    public void create(String title, String content, String nickName, String password) {
//
//        // 게시글 저장
//        anmFrmRepo.save(AnmFrmEntity.getAnmFrmEntity(title, content.getBytes(StandardCharsets.UTF_8), nickName, password));
//    }
//
//    // 게시글 수정
//    @Transactional
//    public AnmFrmPostModRespDTO modify(AnmFrmPostModReqDTO anmFrmPostModReqDTO) {
//
//        // 게시글이 존재하는 지
//        AnmFrmEntity anmFrmEntity = anmFrmRepo.findById(anmFrmPostModReqDTO.getPostSeq()).orElseThrow(() ->
//                new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage()));
//
//        anmFrmEntity.setTitle(anmFrmPostModReqDTO.getTitle());
//        anmFrmEntity.setContent(anmFrmPostModReqDTO.getContent().getBytes(StandardCharsets.UTF_8));
//        anmFrmEntity.setNickName(anmFrmPostModReqDTO.getNickName());
//        anmFrmEntity.setPassword(anmFrmPostModReqDTO.getPassword());
//
//        return AnmFrmPostModRespDTO.getAnmFrmPostModRespDTO(anmFrmRepo.save(anmFrmEntity));
//    }
//
//    // 게시글 삭제
//    @Transactional
//    public void delete(AnmFrmPostDelReqDTO anmFrmPostDelReqDTO) {
//
//        // 게시글이 존재하는 지
//        AnmFrmEntity anmFrmEntity = anmFrmRepo.findById(anmFrmPostDelReqDTO.getPostSeq()).orElseThrow(() ->
//                new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage()));
//
//        // 게시글 삭제 권한 (비밀번호) 체크
//        if (!anmFrmEntity.getPassword().equals(anmFrmPostDelReqDTO.getPassword())) {
//            throw new ApplicationContextException(ErrorCode.INVALID_POST_PERMISSION.getMessage());
//        }
//
//        anmFrmRepo.delete(anmFrmEntity);
//    }
//
//    // 게시글 비밀번호 검증
//    @Transactional
//    public void validate(Long postSeq, String password) {
//
//        // 게시글이 존재하는 지
//        AnmFrmEntity forumEntity = anmFrmRepo.findById(postSeq).orElseThrow(() ->
//                new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage()));
//
//        // 게시글 수정 권한 (게시글을 작성한 사람인지) 체크
//        if (!forumEntity.getPassword().equals(password)) {
//            throw new ApplicationContextException(ErrorCode.INVALID_POST_PERMISSION.getMessage());
//        }
//    }
//}
