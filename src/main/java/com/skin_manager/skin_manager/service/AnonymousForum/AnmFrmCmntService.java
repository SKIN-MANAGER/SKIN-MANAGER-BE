package com.skin_manager.skin_manager.service.AnonymousForum;

import com.skin_manager.skin_manager.exception.ErrorCode;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment.AnmFrmCmntCreateReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.comment.AnmFrmCmntDelReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyCreateReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.reply.AnmFrmReplyDelReqDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.comment.AnmFrmCmntRespDTO;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.response.reply.AnmFrmReplyRespDTO;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmCmntEntity;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmCmntRepo;
import com.skin_manager.skin_manager.repository.anonymous_forum.AnmFrmRepo;
import com.skin_manager.skin_manager.util.CommentDeleteStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnmFrmCmntService {

    private final AnmFrmRepo anmFrmRepo;

    private final AnmFrmCmntRepo anmFrmCmntRepo;

    // 부모 댓글 & 작성일자 내림차순
    @Transactional
    public List<AnmFrmReplyRespDTO> findCommentsByForum(Long postSeq) {
        anmFrmRepo.findById(postSeq);
        return convertNestedStructure(anmFrmCmntRepo.findCommentByAnmFrm(postSeq));
    }


    @Transactional
    public Page<AnmFrmCmntRespDTO> pageList(AnmFrmEntity anmFrmEntity, Pageable pageable) {
        Page<AnmFrmCmntEntity> commentEntities = anmFrmCmntRepo.findCommentEntityByAnmFrm(anmFrmEntity, pageable);
        return commentEntities.map(commentEntity -> AnmFrmCmntRespDTO.entityToAnmFrmCmntRespDTO(commentEntity));
    }

    @Transactional
    public AnmFrmCmntRespDTO createComment(AnmFrmCmntCreateReqDTO anmFrmCmntCreateReqDTO) {

        log.info("postSeq : " + anmFrmCmntCreateReqDTO.getPostSeq());

//        // 2. 회원 존재 여부 검증
//        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
//                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        //
        AnmFrmCmntEntity commentEntity = anmFrmCmntRepo.save(
                AnmFrmCmntEntity.getAnmFrmCmntEntity(
                        anmFrmCmntCreateReqDTO.getNickName(),
                        anmFrmCmntCreateReqDTO.getPassword(),
                        anmFrmRepo.findById(anmFrmCmntCreateReqDTO.getPostSeq()).orElseThrow(() -> new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage())),
                        anmFrmCmntCreateReqDTO.getParentId() != null ? anmFrmCmntRepo.findById(anmFrmCmntCreateReqDTO.getParentId()).orElseThrow(() -> new ApplicationContextException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())) : null,
                        anmFrmCmntCreateReqDTO.getContent()
                ));

        return AnmFrmCmntRespDTO.entityToAnmFrmCmntRespDTO(commentEntity);
    }

    @Transactional
    public AnmFrmReplyRespDTO createReply(AnmFrmReplyCreateReqDTO anmFrmReplyCreateReqDTO) {

        log.info("postSeq : " + anmFrmReplyCreateReqDTO.getPostSeq());

//        // 2. 회원 존재 여부 검증
//        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
//                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s은 존재하지 않는 회원입니다. 회원가입 후 작성 바랍니다.", memberId)));

        //
        AnmFrmCmntEntity anmFrmCmntEntity = anmFrmCmntRepo.save(
                AnmFrmCmntEntity.getAnmFrmCmntEntity(
                        anmFrmReplyCreateReqDTO.getNickName(),
                        anmFrmReplyCreateReqDTO.getPassword(),
                        anmFrmRepo.findById(anmFrmReplyCreateReqDTO.getPostSeq()).orElseThrow(() -> new ApplicationContextException(ErrorCode.POST_NOT_FOUND.getMessage())),
                        anmFrmReplyCreateReqDTO.getParentId() != null ? anmFrmCmntRepo.findById(anmFrmReplyCreateReqDTO.getParentId()).orElseThrow(() -> new ApplicationContextException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())) : null,
                        anmFrmReplyCreateReqDTO.getReply()
                ));

        return AnmFrmReplyRespDTO.entityToAnmFrmReplyRespDTO(anmFrmCmntEntity);
    }

    public long countComment(Long postSeq) {

        return anmFrmCmntRepo.countAnmFrmCmntEntityByPostSeq(postSeq);
    }

    // 댓글 삭제 (하위 대댓글도 같이 삭제)
    @Transactional
    public void deleteComment(AnmFrmCmntDelReqDTO anmFrmCmntDelReqDTO) {
        AnmFrmCmntEntity anmFrmCmntEntity = anmFrmCmntRepo.findCommentEntityByCommentIdWithParentCommentAndPassword(anmFrmCmntDelReqDTO.getCommentId(), anmFrmCmntDelReqDTO.getPassword());

        if (anmFrmCmntEntity == null) {
            // 예외 발생 또는 적절한 처리
            throw new IllegalArgumentException("잘못된 비밀번호 또는 댓글 ID 입니다.");
        }

        if(anmFrmCmntEntity.getChildComments().size() != 0) {
            anmFrmCmntEntity.changeCommentDeleteStatus(CommentDeleteStatus.Y);
        }
        else {
            anmFrmCmntRepo.delete(getDeletableAncestorComment(anmFrmCmntEntity));
        }
    }

    // 대댓글 삭제
    @Transactional
    public void deleteReply(AnmFrmReplyDelReqDTO anmFrmReplyDelReqDTO) {
        AnmFrmCmntEntity commentEntity = anmFrmCmntRepo.findCommentEntityByCommentIdWithParentIdAndPassword(anmFrmReplyDelReqDTO.getCommentId(), anmFrmReplyDelReqDTO.getParentId(), anmFrmReplyDelReqDTO.getPassword());

        anmFrmCmntRepo.delete(getDeletableAncestorComment(commentEntity));
    }

    private AnmFrmCmntEntity getDeletableAncestorComment(AnmFrmCmntEntity commentEntity) {
        AnmFrmCmntEntity parentComment = commentEntity.getParentComment();

        if(parentComment != null && parentComment.getChildComments().size() == 1 && parentComment.getIsDeleted() == CommentDeleteStatus.Y)
            return getDeletableAncestorComment(parentComment);

        return commentEntity;
    }

    private List<AnmFrmReplyRespDTO> convertNestedStructure(List<AnmFrmCmntEntity> commentEntities) {
        List<AnmFrmReplyRespDTO> result = new ArrayList<>();

        Map<Long, AnmFrmReplyRespDTO> map = new HashMap<>();
        commentEntities.stream().forEach(c -> {
            AnmFrmReplyRespDTO anmFrmReplyRespDTO = AnmFrmReplyRespDTO.entityToAnmFrmReplyRespDTO(c);
            map.put(anmFrmReplyRespDTO.getId(), anmFrmReplyRespDTO);

            if(c.getParentComment() != null)
                map.get(c.getParentComment().getCommentId()).getChildComments().add(anmFrmReplyRespDTO);
            else
                result.add(anmFrmReplyRespDTO);
        });

        return result;
    }
}
