package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentMoreDto;
import com.react.voteproject.dto.CommentResponseDto;
import com.react.voteproject.dto.CommentUpdateDto;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.repository.CommentRepository;
import com.react.voteproject.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
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
    public Boolean write(CommentWriteDto commentWriteDto) {
        Optional<Vote> vote = voteRepository.findById(commentWriteDto.getVote_id());
        if(vote.isPresent()){
            Optional<Comment> savedComent = Optional.of(commentRepository.save(commentWriteDto.createComment(vote.get(), AuthContext.getAuth())));
            if(savedComent.isPresent())
            {
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }
    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentMoreDto findComment(Long id, Pageable pageable) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()){
            int page = pageable.getPageNumber();
            PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
            Slice<Comment> comments = commentRepository.findSliceByVote(vote.get(), pageRequest);
            List<CommentResponseDto> commentResponseDto = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());
            Long total = commentRepository.countCommentsByVote(vote.get());
            CommentMoreDto commentMoreDto = CommentMoreDto.createCommentMoreDto(total,comments,commentResponseDto);
            return commentMoreDto;
        }
        else {
            return new CommentMoreDto();
        }

    }
    // 댓글 수정
    @Transactional
    public Boolean update(CommentUpdateDto commentUpdateDto) {
        Optional<Vote> vote = voteRepository.findById(commentUpdateDto.getVote_id());
        if(vote.isPresent()){
            Optional<Comment> comment = commentRepository.findById(commentUpdateDto.getComment_id());
            if(comment.isPresent())
            {
                // 댓글 작성자와 일치하지 않다면
                if(!comment.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                    return false;
                }

                // 댓글 수정
                comment.get().changeContent(commentUpdateDto.getContent());
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }
    // 댓글 삭제
    @Transactional
    public Boolean delete(Long commentId) {

            Optional<Comment> comment = commentRepository.findById(commentId);
            if(comment.isPresent())
            {
                // 댓글 작성자와 일치하지 않다면
                if(!comment.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                    return false;
                }
                // 댓글 삭제
                commentRepository.delete(comment.get());
                return true;
            }
            return false;

    }
}
