package com.react.voteproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.PublicKey;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentMoreDto {

    private int page;
    private Boolean hasContent;
    private Boolean hasNext;
    private List<CommentResponseDto> comment;

    public static CommentMoreDto createCommentMoreDto(int page,Boolean hasContent,Boolean hasNext,List<CommentResponseDto> comment) {

        return CommentMoreDto.builder().page(page).hasContent(hasContent).hasNext(hasNext).comment(comment).build();

    }
}
