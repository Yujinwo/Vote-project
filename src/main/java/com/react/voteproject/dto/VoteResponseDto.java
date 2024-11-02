package com.react.voteproject.dto;


import com.react.voteproject.entity.Vote;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponseDto {


    private String title;


    private String category;


    private int up;


    private Long commentCount;


    private LocalDateTime startDay;


    private LocalDateTime endDay;

    private List<VoteOptionDto> voteOptions;

    private int optionCountTotal;
    private UserDto user;


    public static VoteResponseDto createVoteResponseDto(Vote vote,Long commentCount) {

       int total = vote.getOptions().stream().mapToInt(o -> o.getCount()).sum();

       return VoteResponseDto.builder()
                .title(vote.getTitle())
                .category(vote.getCategory())
                .up(vote.getUp())
                .commentCount(commentCount)
                .startDay(vote.getStartDay()).endDay(vote.getEndDay())
                .optionCountTotal(total)
                .user(UserDto.createUserDto(vote.getUser()))
                .voteOptions(vote.getOptions().stream().map(o -> VoteOptionDto.createOptionDto(o,total)).collect(Collectors.toList())).build();

    }

}
