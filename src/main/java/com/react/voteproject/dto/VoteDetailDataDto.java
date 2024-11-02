package com.react.voteproject.dto;


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
    private Boolean hasNext ;

    // 좋아요 활성화 여부
    private Boolean hasUp = false;

    // 북마크 활성화 여부
    private Boolean hasBookMark  = false;

    public static VoteDetailDataDto createVoteDetailDataDto(VoteResponseDto voteResponseDto, Long selectedOptionId, List<CommentResponseDto> comments, Boolean hasNext, Boolean hasUp, Boolean hasBookmark)
    {
        return VoteDetailDataDto.builder().vote(voteResponseDto).selectedOptionId(selectedOptionId).comments(comments).hasNext(hasNext).hasUp(hasUp).hasBookMark(hasBookmark).build();
    }
}
