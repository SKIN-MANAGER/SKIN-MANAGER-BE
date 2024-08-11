package com.skin_manager.skin_manager.controller;

import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment.AnmFrmCmntCreateReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment.AnmFrmCmntDelReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyCreateReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyDelReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.comment.AnmFrmCmntRespDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.reply.AnmFrmReplyRespDTO;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import com.skin_manager.skin_manager.service.AnonymousForum.AnmFrmCmntService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class AnmFrmCmntController {

    private final AnmFrmCmntService anmFrmCmntService;

    // 게시글에 (대)댓글 작성
    @PostMapping("/create")
    public ResponseEntity<AnmFrmCmntRespDTO> createComment(@RequestBody AnmFrmCmntCreateReqDTO anmFrmCmntCreateReqDTO) {

//        MemberDTO memberDTO = memberService.getMemberByMemberId(customMemberDetails.getUsername());

        log.info("content : " + anmFrmCmntCreateReqDTO.getContent());
        log.info("postSeq : " + anmFrmCmntCreateReqDTO.getPostSeq());
        log.info("nickName : " + anmFrmCmntCreateReqDTO.getNickName());
        log.info("password : " + anmFrmCmntCreateReqDTO.getPassword());
        log.info("parentId : " + anmFrmCmntCreateReqDTO.getParentId());
        log.info("isDeleted : " + anmFrmCmntCreateReqDTO.getIsDeleted());

        AnmFrmCmntRespDTO anmFrmCmntRespDTO = anmFrmCmntService.createComment(anmFrmCmntCreateReqDTO);
        return ResponseEntity.ok().body(anmFrmCmntRespDTO);
    }

    // 게시글에 (대)댓글 작성
    @PostMapping("/reply/create")
    public ResponseEntity<AnmFrmReplyRespDTO> createReply(@RequestBody AnmFrmReplyCreateReqDTO anmFrmReplyCreateReqDTO) {

//        MemberDTO memberDTO = memberService.getMemberByMemberId(customMemberDetails.getUsername());

        log.info("content : " + anmFrmReplyCreateReqDTO.getReply());
        log.info("postSeq : " + anmFrmReplyCreateReqDTO.getPostSeq());
        log.info("nickName : " + anmFrmReplyCreateReqDTO.getNickName());
        log.info("password : " + anmFrmReplyCreateReqDTO.getPassword());
        log.info("parentId : " + anmFrmReplyCreateReqDTO.getParentId());
        log.info("isDeleted : " + anmFrmReplyCreateReqDTO.getIsDeleted());

        AnmFrmReplyRespDTO anmFrmReplyRespDTO = anmFrmCmntService.createReply(anmFrmReplyCreateReqDTO);
        return ResponseEntity.ok().body(anmFrmReplyRespDTO);
    }

    // 게시글의 댓글 페이징
    @GetMapping("/list")
    public ResponseEntity<Page<AnmFrmCmntRespDTO>> commentList(@RequestParam AnmFrmEntity anmFrmEntity, @PageableDefault(size = 10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("postSeq : " + anmFrmEntity.getPostSeq());
        log.info("title : " + anmFrmEntity.getTitle());
        log.info("nickName : " + anmFrmEntity.getNickName());
        log.info("content : " + anmFrmEntity.getContent());
        log.info("comments : " + anmFrmEntity.getComments());


        Page<AnmFrmCmntRespDTO> commentDTOS = anmFrmCmntService.pageList(anmFrmEntity, pageable);

        return ResponseEntity.ok().body(commentDTOS);
    }


    // 게시글 댓글의 대댓글 리스트
    @GetMapping("/reply/{postId}")
    public ResponseEntity<List<AnmFrmReplyRespDTO>> replyList(@PathVariable Long postSeq) {
        log.info("postSeq : " + postSeq);
        List<AnmFrmReplyRespDTO> replyDTOS = anmFrmCmntService.findCommentsByForum(postSeq);
        return ResponseEntity.ok().body(replyDTOS);
    }

    // 게시글 카운팅
    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> countCommentEntityByPostId(@PathVariable Long postSeq) {
        log.info("postSeq : " + postSeq);
        long count = anmFrmCmntService.countComment(postSeq);
        log.info("count : " + count);
        return ResponseEntity.ok(count);
    }

    // 댓글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<AnmFrmCmntRespDTO> deleteComment(@RequestBody AnmFrmCmntDelReqDTO anmFrmCmntDelReqDTO) {
        log.info("commentId : " + anmFrmCmntDelReqDTO.getCommentId());
        log.info("password : " + anmFrmCmntDelReqDTO.getPassword());

        anmFrmCmntService.deleteComment(anmFrmCmntDelReqDTO);
        return ResponseEntity.noContent().build();
    }

    // 대댓글 삭제
    @DeleteMapping("/reply/delete")
    public ResponseEntity<AnmFrmCmntRespDTO> deleteReply(@RequestBody AnmFrmReplyDelReqDTO anmFrmReplyDelReqDTO) {

        log.info("parentId : " + anmFrmReplyDelReqDTO.getParentId());
        log.info("commentId : " + anmFrmReplyDelReqDTO.getCommentId());
        log.info("password : " + anmFrmReplyDelReqDTO.getPassword());

        anmFrmCmntService.deleteReply(anmFrmReplyDelReqDTO);
        return ResponseEntity.noContent().build();
    }

//    // 댓글 수정
//    @PutMapping("/modify")
//    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentCreateRequestDto) {
//        CommentDTO commentDTO = commentService.updateComment(commentCreateRequestDto);
//        return ResponseEntity.ok().body(commentDTO);
//    }


}
