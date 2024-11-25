package com.react.voteproject.dto;


import lombok.Getter;

@Getter
public class VoteWithCommentCountDTO {

    private Long id;
    private String title;
    private int up;
    private Long commentCount;

    public VoteWithCommentCountDTO(Long voteId, String voteTitle,int up, Long commentCount) {
        this.id = voteId;
        this.up = up;
        this.title = voteTitle;
        this.commentCount = commentCount;
    }
}
