package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentMoreDto;
import com.react.voteproject.dto.CommentResponseDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

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

    public CommentMoreDto findComment(Long id, Pageable pageable) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()){
            int page = pageable.getPageNumber();


            PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);


            Slice<Comment> comments = commentRepository.findSliceByVote(vote.get(), pageRequest);

            List<CommentResponseDto> commentResponseDto = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());

            CommentMoreDto commentMoreDto = CommentMoreDto.createCommentMoreDto(comments.getNumber(),comments.hasContent(),comments.hasNext(),commentResponseDto);
            return commentMoreDto;
        }
        else {
            return new CommentMoreDto();
        }

    }
}
