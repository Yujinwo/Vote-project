package com.react.voteproject.service;


import com.react.voteproject.dto.VoteDetailDataDto;
import com.react.voteproject.dto.VoteResponseDto;
import com.react.voteproject.dto.VoteWriteDto;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.entity.VoteOption;
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

    public VoteDetailDataDto findVote(Long id) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()) {
            VoteResponseDto voteResponseDto = vote.map(v -> VoteResponseDto.builder().title(v.getTitle()).category(v.getCategory()).up(v.getUp()).commentCount(v.getCommentCount()).startDay(v.getStartDay()).endDay(v.getEndDay()).voteoptions(v.getOptions().stream().map(o -> o.getContent()).collect(Collectors.toList())).build()).get();
            VoteDetailDataDto detailDataDto = vote.map(v -> VoteDetailDataDto.builder().vote(voteResponseDto).build()).get();
            return detailDataDto;
        }
        else {
            return new VoteDetailDataDto();
        }

    }
}
