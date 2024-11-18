package com.react.voteproject.dto;


import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteHomeDataDto {

    private List<VoteResponseDto> vote;
    private Boolean hasNext;
    private int page;

    public static VoteHomeDataDto createVoteHomeDataDto(List<VoteResponseDto> vote, Boolean hasNext, int page) {
        return VoteHomeDataDto.builder()
                .vote(vote)
                .hasNext(hasNext)
                .page(page)
                .build();

    }
}
