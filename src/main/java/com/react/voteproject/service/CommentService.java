package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentMoreDto;
import com.react.voteproject.dto.CommentResponseDto;
import com.react.voteproject.dto.CommentUpdateDto;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.exception.CreationException;
import com.react.voteproject.exception.UnauthorizedException;
import com.react.voteproject.repository.CommentRepository;
import com.react.voteproject.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.CheckReturnValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    // 댓글 작성
    @Transactional
    public void write(CommentWriteDto commentWriteDto) {
        Vote vote = voteRepository.findById(commentWriteDto.getVote_id())
                .orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));

        Comment savedComment = Optional.of(
                    commentRepository.save(commentWriteDto.createComment(vote, AuthContext.getAuth()))
            ).orElseThrow(() -> new CreationException("댓글 작성을 실패했습니다. 잠시 후 다시 시도해 주세요!"));
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentMoreDto findComment(Long id, Pageable pageable) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()){
            int page = pageable.getPageNumber();
            PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
            Slice<Comment> comments = commentRepository.findPageByVote(vote.get(), pageRequest);
            List<CommentResponseDto> commentResponseDto = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());
            CommentMoreDto commentMoreDto = CommentMoreDto.createCommentMoreDto(comments,commentResponseDto);
            return commentMoreDto;
        }
        else {
            return new CommentMoreDto();
        }

    }
    // 댓글 수정
    @Transactional
    public void update(CommentUpdateDto commentUpdateDto) {
        Vote vote = voteRepository.findById(commentUpdateDto.getVote_id())
                .orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentUpdateDto.getComment_id())
                .orElseThrow(() -> new CreationException("댓글이 존재하지 않습니다."));
        // 댓글 작성자와 일치하지 않다면
        if(!comment.getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                throw new UnauthorizedException("댓글 작성자와 일치하지 않습니다.");
        }
        // 댓글 수정
        comment.changeContent(commentUpdateDto.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CreationException("댓글이 존재하지 않습니다."));
            // 댓글 작성자와 일치하지 않다면
            if(!comment.getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                throw new UnauthorizedException("댓글 작성자와 일치하지 않습니다.");
            }
            // 댓글 삭제
            commentRepository.delete(comment);
    }

}
