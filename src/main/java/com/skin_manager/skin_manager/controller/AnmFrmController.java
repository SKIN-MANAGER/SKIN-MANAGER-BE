package com.skin_manager.skin_manager.controller;

import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post.AnmFrmPostCreateReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post.AnmFrmPostDelReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.post.AnmFrmPostModReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.search.AnmFrmSrchCondDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.AnmFrmRespDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.page.AnmFrmPagingResultDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.post.AnmFrmPostModRespDTO;
import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmCmntRepo;
import com.skin_manager.skin_manager.service.AnonymousForum.AnmFrmService;
import com.skin_manager.skin_manager.util.ResponseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum")
@Slf4j
@RequiredArgsConstructor
public class AnmFrmController {

    private final AnmFrmService anmFrmService;

    // 게시글 조건부 전체 조회
    @GetMapping("/list")
    public AnmFrmPagingResultDTO<List<AnmFrmRespDTO>> getAnmFrmList(@PageableDefault(sort = {"registeredAt"}) Pageable pageable, AnmFrmSrchCondDTO anmFrmSrchCondDTO) throws Exception {

        log.info("searchCategory : " + anmFrmSrchCondDTO.getSearchCategory());
        log.info("searchValue : " + anmFrmSrchCondDTO.getSearchValue());

        return anmFrmService.getAnmFrmList(pageable, anmFrmSrchCondDTO);
    }

    // 게시글 작성
    @PostMapping("/post")
    public ResponseResultCode<Void> create(@RequestBody AnmFrmPostCreateReqDTO anmFrmPostCreateReqDTO) {
        log.info("title : " + anmFrmPostCreateReqDTO.getTitle());
        log.info("content : " + anmFrmPostCreateReqDTO.getContent());
        log.info("nickName : " + anmFrmPostCreateReqDTO.getNickName());
        log.info("password : " + anmFrmPostCreateReqDTO.getPassword());
        anmFrmService.create(anmFrmPostCreateReqDTO.getTitle(), anmFrmPostCreateReqDTO.getContent(), anmFrmPostCreateReqDTO.getNickName(), anmFrmPostCreateReqDTO.getPassword());
        return ResponseResultCode.success();
    }


    // 게시글 수정
    @PutMapping("/modify")
    public ResponseResultCode<AnmFrmPostModRespDTO> modify(@RequestBody AnmFrmPostModReqDTO anmFrmPostModReqDTO) {

        System.out.println("========================================");
        System.out.println("postSeq : " + anmFrmPostModReqDTO.getPostSeq());
        System.out.println("title : " + anmFrmPostModReqDTO.getTitle());
        System.out.println("content : " + anmFrmPostModReqDTO.getContent());
        System.out.println("nickName : " + anmFrmPostModReqDTO.getNickName());
        System.out.println("password : " + anmFrmPostModReqDTO.getPassword());
        System.out.println("========================================");

        AnmFrmPostModRespDTO postModifyResponseDTO = anmFrmService.modify(anmFrmPostModReqDTO);

        return ResponseResultCode.success(postModifyResponseDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseResultCode<Void> delete(@RequestBody AnmFrmPostDelReqDTO anmFrmPostDelReqDTO) {

        log.info("postSeq : " + anmFrmPostDelReqDTO.getPostSeq());
        log.info("password : " + anmFrmPostDelReqDTO.getPassword());

        anmFrmService.delete(anmFrmPostDelReqDTO);
        return ResponseResultCode.success();
    }

    // 게시글 비밀번호 확인
    @GetMapping("/validate")
    public ResponseResultCode<Void> validate(@RequestParam Long postSeq, @RequestParam String password) {

        log.info("postSeq : " + postSeq);
        log.info("password : " + password);

        anmFrmService.validate(postSeq, password);
        return ResponseResultCode.success();
    }

    // 게시글 단건 조회
    @GetMapping("/post/{postSeq}")
    public ResponseResultCode<AnmFrmRespDTO> getAnmFrmPost(@PathVariable Long postSeq) {

        log.info("postSeq : " + postSeq);

        AnmFrmRespDTO anmFrmRespDTO = anmFrmService.getAnmFrmPost(postSeq);
        return ResponseResultCode.success(anmFrmRespDTO);
    }
}
