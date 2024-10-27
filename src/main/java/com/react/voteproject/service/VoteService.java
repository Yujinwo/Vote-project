package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.VoteDetailDataDto;
import com.react.voteproject.dto.VoteResponseDto;
import com.react.voteproject.dto.VoteWriteDto;
import com.react.voteproject.entity.UserVote;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.entity.VoteOption;
import com.react.voteproject.repository.UserVoteRepository;
import com.react.voteproject.repository.VoteOptionRepository;
import com.react.voteproject.repository.VoteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
     private final VoteRepository voteRepository;
     private final VoteOptionRepository voteOptionRepository;
     private final UserVoteRepository userVoteRepository;
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
    @Transactional(readOnly = true)
    public VoteDetailDataDto findVote(Long id) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()) {
            Optional<UserVote> userVote = userVoteRepository.findByuserID(AuthContext.getAuth());
            VoteResponseDto voteResponseDto = vote.map(VoteResponseDto::createVoteResponseDto).get();
            if(userVote.isPresent())
            {
                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userVote.get().getVoteOption().getId());
            }
            else {

                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,null);
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
}
