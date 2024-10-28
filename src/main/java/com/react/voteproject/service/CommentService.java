package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.repository.CommentRepository;
import com.react.voteproject.repository.VoteRepository;
import jakarta.validation.ReportAsSingleViolation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
