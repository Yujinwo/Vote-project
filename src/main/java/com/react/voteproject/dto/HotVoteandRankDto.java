package com.react.voteproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotVoteandRankDto {
    List<HotVoteResponseDto> vote;


    public static HotVoteandRankDto createHotVoteandRankDto(List<HotVoteResponseDto> vote) {
        return HotVoteandRankDto.builder().vote(vote).build();
    }
}
