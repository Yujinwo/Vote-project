package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.UserVote;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.entity.VoteOption;
import com.react.voteproject.repository.CommentRepository;
import com.react.voteproject.repository.UserVoteRepository;
import com.react.voteproject.repository.VoteOptionRepository;
import com.react.voteproject.repository.VoteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
     private final VoteRepository voteRepository;
     private final VoteOptionRepository voteOptionRepository;
     private final UserVoteRepository userVoteRepository;
     private final CommentRepository commentRepository;
     private final EntityManager em;


     @Transactional
     public Boolean write(VoteWriteDto voteWriteDto) {

        Optional<Vote> vote = Optional.of(voteRepository.save(voteWriteDto.createVote()));
        if(vote.isEmpty())
        {
            return false;
        }
        em.flush();
        em.clear();

        for (String option : voteWriteDto.getChoices())
        {
            Optional<VoteOption> voteOption = Optional.ofNullable(voteOptionRepository.save(VoteOption.builder().content(option).vote(vote.get()).build()));
            if(voteOption.isEmpty())
            {
                return false;
            }
        }

        return true;

    }

    @Transactional
    public Boolean update(VoteUpdateDto voteUpdateDto) {

        Optional<Vote> vote = voteRepository.findById(voteUpdateDto.getVote_id());
        if(vote.isPresent()){

                // 댓글 작성자와 일치하지 않다면
                if(!vote.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                    return false;
                }
                List<LocalDateTime> dateTimeList = voteUpdateDto.changeDayFormat();
                vote.get().changeVoteContent(voteUpdateDto.getTitle(), voteUpdateDto.getCategory(), dateTimeList.get(0),dateTimeList.get(1));
                List<VoteOption> voteOption = voteOptionRepository.findByvote(vote.get());
                if(!voteOption.isEmpty()) {

                    for (int i = 0; i < voteOption.size(); i++) {
                        String option = voteOption.get(i).getContent();
                        if(!voteUpdateDto.getChoices().get(i).equals(option)) {
                            voteOption.get(i).changeContent(voteUpdateDto.getChoices().get(i));
                        }
                    }

                    return true;

                }
        }
        return false;
    }
    @Transactional(readOnly = true)
    public VoteDetailDataDto findvotes(Long id) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()) {
            // 이미 투표 선택했는지 조회
            Optional<UserVote> userVote = userVoteRepository.findByuserID(AuthContext.getAuth());
            // 투표 데이터 조회
            VoteResponseDto voteResponseDto = vote.map(VoteResponseDto::createVoteResponseDto).get();

            // 투표 댓글 Slice 조회
            PageRequest pageRequest = PageRequest.of(0,10);
            Slice<Comment> comments = commentRepository.findSliceByVote(vote.get(), pageRequest);
            List<CommentResponseDto> commentList = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());

            if(userVote.isPresent())
            {
                Long userSelectedId = userVote.get().getVoteOption().getId();
                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext());
            }
            else {
                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,null,commentList,comments.hasNext());
            }

        }
        else {
            return new VoteDetailDataDto();
        }

    }

    @Transactional
    public Boolean changeVoteOption(Long id) {
        Optional<VoteOption> voteOption = voteOptionRepository.findById(id);
        if(voteOption.isPresent()){
            VoteOption option = voteOption.get();
            Optional<UserVote> uv = userVoteRepository.findByVoteOptionId(option);
            if(uv.isPresent()){
                return false;
            }
            // 기존 선택지 삭제 -> 변경한 선택지로 수정

            Optional<UserVote> userVote = userVoteRepository.findByuserID(AuthContext.getAuth());
            if(userVote.isPresent())
            {
                userVoteRepository.deleteByuserID(AuthContext.getAuth());
                userVote.get().getVoteOption().downCount();
                option.upCount();
            }
            else {
                option.upCount();
            }

            Optional<UserVote> save = Optional.of(userVoteRepository.save(voteOption.get().createUserVote()));
            if(save.isEmpty())
            {
                return false;
            }
            return true;

        }
        return false;
    }


    public Boolean delete(Long voteId) {

        Optional<Vote> vote = voteRepository.findById(voteId);
        if(vote.isPresent())
        {
            // 댓글 작성자와 일치하지 않다면
            if(!vote.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                return false;
            }
            // 투표 삭제
            voteRepository.delete(vote.get());
            return true;
        }
        return false;
    }
}
