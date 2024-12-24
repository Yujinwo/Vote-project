package com.react.voteproject.dto;


import com.react.voteproject.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

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

    public static CommentMoreDto createCommentMoreDto(Slice<Comment> comments, List<CommentResponseDto> comment) {
        return CommentMoreDto.builder()
                .page(comments.getNumber())
                .hasContent(comments.hasContent())
                .hasNext(comments.hasNext())
                .comment(comment)
                .build();
    }
}
