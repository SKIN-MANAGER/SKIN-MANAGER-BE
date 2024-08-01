//package com.skin_manager.skin_manager.service.AnonymousForum;
//
//import com.skin_manager.skin_manager.exception.ErrorCode;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyCreateReqDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyDelReqDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.comment.AnmFrmCmntRespDTO;
//import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.reply.AnmFrmReplyRespDTO;
//import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmCmntEntity;
//import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
//import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmCmntRepo;
//import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmCmntRepoImpl;
//import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmRepo;
//import com.skin_manager.skin_manager.util.CommentDeleteStatus;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AnmFrmCmntService {
//
//    private final AnmFrmRepo anmFrmRepo;
//
//    private final AnmFrmCmntRepo anmFrmCmntRepo;
//
//    // 부모 댓글 & 작성일자 내림차순
//    @Transactional
//    public List<AnmFrmReplyRespDTO> findCommentsByForum(Long postId) {
//        anmFrmRepo.findById(postId);
//        return convertNestedStructure(anmFrmCmntRepo.findCommentByForum(postId));
//    }
//
//
//    @Transactional
//    public Page<AnmFrmCmntRespDTO> pageList(AnmFrmEntity forum, Pageable pageable) {
//        Page<AnmFrmCmntEntity> commentEntities = anmFrmCmntRepo.findCommentEntityByForum(forum, pageable);
//        return commentEntities.map(commentEntity -> AnmFrmCmntRespDTO.entityToCommentDTO(commentEntity));
//    }
//
//    @Transactional
//    public AnmFrmCmntRespDTO createComment(CommentRequestDTO commentRequestDTO) {
//
//        log.info("postId : " + commentRequestDTO.getPostId());
//
////        // 2. 회원 존재 여부 검증
////        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
////                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));
//
//        //
//        AnmFrmCmntEntity commentEntity = anmFrmCmntRepo.save(
//                AnmFrmCmntEntity.getCommentEntity(
//                        commentRequestDTO.getNickName(),
//                        commentRequestDTO.getPassword(),
//                        forumRepository.findById(commentRequestDTO.getPostId()).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", commentRequestDTO.getPostId()))),
//                        commentRequestDTO.getParentId() != null ? commentRepository.findById(commentRequestDTO.getParentId()).orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, String.format("%s번 댓글을 찾을 수 없습니다.", commentRequestDTO.getParentId()))) : null,
//                        commentRequestDTO.getContent()
//                ));
//
//        return AnmFrmCmntRespDTO.entityToCommentDTO(commentEntity);
//    }
//
//    @Transactional
//    public AnmFrmReplyRespDTO createReply(AnmFrmReplyCreateReqDTO replyRequestDTO) {
//
//        log.info("postId : " + replyRequestDTO.getPostId());
//
////        // 2. 회원 존재 여부 검증
////        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
////                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));
//
//        //
//        AnmFrmCmntEntity anmFrmCmntEntity = anmFrmCmntRepo.save(
//                AnmFrmCmntEntity.getAnmFrmCmntEntity(
//                        replyRequestDTO.getNickName(),
//                        replyRequestDTO.getPassword(),
//                        forumRepository.findById(replyRequestDTO.getPostId()).orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s번 게시글을 찾을 수 없습니다.", replyRequestDTO.getPostId()))),
//                        replyRequestDTO.getParentId() != null ? commentRepository.findById(replyRequestDTO.getParentId()).orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, String.format("%s번 댓글을 찾을 수 없습니다.", replyRequestDTO.getParentId()))) : null,
//                        replyRequestDTO.getReply()
//                ));
//
//        return AnmFrmReplyRespDTO.entityToAnmFrmReplyRespDTO(anmFrmCmntEntity);
//    }
//
//    public long countComment(Long postId) {
//
//        return anmFrmCmntRepo.countCommentEntityByPostId(postId);
//    }
//
//    // 댓글 삭제 (하위 대댓글도 같이 삭제)
//    @Transactional
//    public void deleteComment(CommentDeleteRequestDTO commentDeleteRequestDTO) {
//        AnmFrmCmntEntity anmFrmCmntEntity = commentRepository.findCommentEntityByCommentIdWithParentCommentAndPassword(commentDeleteRequestDTO.getCommentId(), commentDeleteRequestDTO.getPassword());
//
//        if (anmFrmCmntEntity == null) {
//            // 예외 발생 또는 적절한 처리
//            throw new IllegalArgumentException("잘못된 비밀번호 또는 댓글 ID 입니다.");
//        }
//
//        if(anmFrmCmntEntity.getChildComments().size() != 0) {
//            anmFrmCmntEntity.changeCommentDeleteStatus(CommentDeleteStatus.Y);
//        }
//        else {
//            commentRepository.delete(getDeletableAncestorComment(anmFrmCmntEntity));
//        }
//    }
//
//    // 대댓글 삭제
//    @Transactional
//    public void deleteReply(AnmFrmReplyDelReqDTO anmFrmReplyDelReqDTO) {
//        AnmFrmCmntEntity commentEntity = commentRepository.findCommentEntityByCommentIdWithParentIdAndPassword(anmFrmReplyDelReqDTO.getCommentId(), anmFrmReplyDelReqDTO.getParentId(), anmFrmReplyDelReqDTO.getPassword());
//
//        commentRepository.delete(getDeletableAncestorComment(commentEntity));
//    }
//
//    private AnmFrmCmntEntity getDeletableAncestorComment(AnmFrmCmntEntity commentEntity) {
//        AnmFrmCmntEntity parentComment = commentEntity.getParentComment();
//
//        if(parentComment != null && parentComment.getChildComments().size() == 1 && parentComment.getIsDeleted() == CommentDeleteStatus.Y)
//            return getDeletableAncestorComment(parentComment);
//
//        return commentEntity;
//    }
//
//    private List<AnmFrmReplyRespDTO> convertNestedStructure(List<AnmFrmCmntEntity> commentEntities) {
//        List<AnmFrmReplyRespDTO> result = new ArrayList<>();
//
//        Map<Long, AnmFrmReplyRespDTO> map = new HashMap<>();
//        commentEntities.stream().forEach(c -> {
//            AnmFrmReplyRespDTO anmFrmReplyRespDTO = AnmFrmReplyRespDTO.entityToAnmFrmReplyRespDTO(c);
//            map.put(anmFrmReplyRespDTO.getId(), anmFrmReplyRespDTO);
//
//            if(c.getParentComment() != null)
//                map.get(c.getParentComment().getCommentId()).getChildComments().add(anmFrmReplyRespDTO);
//            else
//                result.add(anmFrmReplyRespDTO);
//        });
//
//        return result;
//    }
//}
