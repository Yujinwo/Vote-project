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

    private VoteResponseDto vote;
    private List<CommentResponseDto> comments;

    // 유저가 선택한 항목
    private Long selectedOptionId;

    // 댓글 더보기 활성화 여부
    private Boolean hasNext;

    public static VoteDetailDataDto createVoteDetailDataDto(VoteResponseDto voteResponseDto,Long selectedOptionId,List<CommentResponseDto> comments,Boolean hasNext)
    {
        return VoteDetailDataDto.builder().vote(voteResponseDto).selectedOptionId(selectedOptionId).comments(comments).hasNext(hasNext).build();
    }
}
