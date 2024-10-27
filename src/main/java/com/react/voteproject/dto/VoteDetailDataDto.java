package com.react.voteproject.dto;


import com.react.voteproject.entity.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDetailDataDto {

    VoteResponseDto vote;
    Long selectedOptionId;

    public static VoteDetailDataDto createVoteDetailDataDto(VoteResponseDto voteResponseDto,Long selectedOptionId)
    {
        return VoteDetailDataDto.builder().vote(voteResponseDto).selectedOptionId(selectedOptionId).build();
    }
}
