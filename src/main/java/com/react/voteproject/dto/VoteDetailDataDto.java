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
    List<CommentResponseDto> comments;

    public static VoteDetailDataDto createVoteDetailDataDto(VoteResponseDto voteResponseDto,Long selectedOptionId,List<CommentResponseDto> comments)
    {
        return VoteDetailDataDto.builder().vote(voteResponseDto).selectedOptionId(selectedOptionId).comments(comments).build();
    }
}
